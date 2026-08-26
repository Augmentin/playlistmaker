package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerFragment

import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.search.ui.view_model.HistoryViewModel
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.util.debounce
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class SearchFragment  : Fragment() {

    private var current_search: String = STRING_DEF

    companion object {

        const val STRING_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: SongListAdapter

    private lateinit var onTrackClickDebounce: (TrackData) -> Unit
    private lateinit var onHistoryClickDebounce: (TrackData) -> Unit
    private lateinit var historyAdapter: SongListAdapter



    private val viewModel by viewModel<SearchViewModel>()
    private val historyListModel by viewModel<HistoryViewModel>()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.searchView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onTrackClickDebounce = debounce<TrackData>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false)
        { track ->
            openPlayer(track, true)
        }
        onHistoryClickDebounce = debounce<TrackData>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false)
        { track ->
            openPlayer(track, false)
        }
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        adapter = SongListAdapter( { track ->
            onTrackClickDebounce(track)
        })
        historyAdapter = SongListAdapter( { track ->
             onHistoryClickDebounce(track)
        })
        historyListModel.observeState().observe(viewLifecycleOwner){
            historyAdapter.trackList.clear()
            historyAdapter.trackList.addAll(it as ArrayList<TrackData>)
            historyAdapter.notifyDataSetChanged()
        }

        binding.songItems.adapter = adapter
        binding.historySongItems.adapter = historyAdapter


        binding.historyRefresh.setOnClickListener {
            historyListModel.clear()
            binding.searchHistoryGroup.isVisible = false
        }



        binding.inputEditText.setText(current_search)

        binding.clearIcon.setOnClickListener {
            hideKeyboard()
            viewModel.clearSearch()
            binding.inputEditText.setText("")
            binding.inputEditText.clearFocus()
            binding.inputEditText.setHint(R.string.search_hint)
            adapter.trackList.clear()
            showEmptyForm()
            adapter.notifyDataSetChanged()
        }
        binding.refresh.setOnClickListener {
            viewModel.searchNow(current_search)
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, event  ->

            if (actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchNow(current_search)
            }
            false
        }

        binding.inputEditText.doOnTextChanged { s: CharSequence?, start: Int, before: Int, count: Int ->
            if (!s.isNullOrEmpty()) {
                binding.inputEditText.hint = ""
                current_search = s.toString()
            } else {
                current_search = STRING_DEF
            }

            showEmptyForm()
            binding.searchHistoryGroup.isVisible = binding.inputEditText.hasFocus() && s?.isEmpty() == true
            binding.clearIcon.isVisible = !s.isNullOrEmpty()
            viewModel.searchDebounce(current_search)


        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistoryGroup.isVisible =
                historyListModel.size() > 0 && hasFocus && binding.inputEditText.text.isEmpty()

        }
        viewModel.updateFavorites()
        historyListModel.updateFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

     //   viewModel.updateFavorites()
      //  historyListModel.updateFavorites()
    }
     fun hideKeyboard() {
        val imm = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
    fun render(state: SearchState) {
        showEmptyForm()


        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError(state.errorMessage, state.errorPlaceholderMessage, state.img)
            is SearchState.Empty -> showEmpty(state.message, state.img)
        }
    }

    fun showEmpty(message: String, int: Int){
        if(int != -1){
            showMessage(message, "", int)
        }

    }
    fun showError(errorMessage: String, errorPlaceholderMessage: String? = null, img: Int? = null ){
        if (errorMessage.isNotEmpty()) {
            showMessage(errorMessage, errorPlaceholderMessage, img)
            binding.refresh.isVisible = true
        }
    }
    fun showContent(requestedTrackList: List<TrackData>){
        binding.songItems.isVisible = true
        adapter.trackList.clear()
        adapter.trackList.addAll(requestedTrackList)
        adapter.notifyDataSetChanged()
    }
    fun showLoading(){
        binding.progressBar.isVisible = true
    }

    private fun openPlayer(track: TrackData, addToHistory: Boolean) {
        if (addToHistory) {
            historyListModel.add(track)
        }

        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(Gson().toJson(track))
        )
    }
    private fun showEmptyForm() {
        binding.progressBar.isVisible = false
        binding.songItems.isVisible = false
        binding.searchHistoryGroup.isVisible = false
        binding.placeholderTitle.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.failImg.isVisible = false
        binding.refresh.isVisible = false

    }

    private fun showMessage(
        textTitle: String,
        textMessage: String? = "",
        imageResource: Int? = null
    ) {
        binding.placeholderTitle.visibility = View.VISIBLE
        binding.placeholderTitle.text = textTitle
        if (!textMessage.isNullOrBlank()) {
            binding.placeholderMessage.text = textMessage
            binding.placeholderMessage.visibility = View.VISIBLE
        }
        if (imageResource != null) {
            binding.failImg.setImageResource(imageResource)
            binding.failImg.visibility = View.VISIBLE
        }
    }
}