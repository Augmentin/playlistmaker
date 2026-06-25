package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager


import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.api.HistoryInteractor

import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.presentation.playlist.PlaylistActivity
import com.example.playlistmaker.search.ui.view_model.HistoryViewModel
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

import com.google.gson.Gson


class SearchActivity : AppCompatActivity() {

    private var current_search: String = STRING_DEF

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val STRING_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var binding: ActivitySearchBinding

    private lateinit var adapter: SongListAdapter


    private lateinit var historyAdapter: SongListAdapter

    private var isClickAllowed = true


    private val handler = Handler(Looper.getMainLooper())
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }


    private var viewModel: SearchViewModel? = null
    private var historyListModel: HistoryViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.searchView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, SearchViewModel.getFactory())
            .get(SearchViewModel::class.java)
        historyListModel = ViewModelProvider(this, HistoryViewModel.getFactory())
            .get(HistoryViewModel::class.java)
        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        adapter = SongListAdapter( { track ->
            if (clickDebounce()) {
                historyListModel?.add(track)
                val displayIntent = Intent(this, PlaylistActivity::class.java)
                displayIntent.putExtra("TRACK", Gson().toJson(track))
                startActivity(displayIntent)
            }
        })
        historyAdapter = SongListAdapter( { track ->
            if (clickDebounce()) {
                val displayIntent = Intent(this, PlaylistActivity::class.java)
                displayIntent.putExtra("TRACK", Gson().toJson(track))
                startActivity(displayIntent)
            }
        })
        historyListModel?.observeState()?.observe(this){
            historyAdapter.trackList = it as ArrayList<TrackData>
            historyAdapter.notifyDataSetChanged()
        }

        binding.songItems.adapter = adapter
        binding.historySongItems.adapter = historyAdapter


        binding.backToolbar.setNavigationOnClickListener {
            finish()
        }
        binding.historyRefresh.setOnClickListener {
            historyListModel?.clear()
            binding.searchHistoryGroup.isVisible = false
        }



        binding.inputEditText.setText(current_search)

        binding.clearIcon.setOnClickListener {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
            binding.inputEditText.setText("")
            binding.inputEditText.clearFocus()
            binding.inputEditText.setHint(R.string.search_hint)
            adapter.trackList.clear()
            showEmptyForm()
            adapter.notifyDataSetChanged()
        }
        binding.refresh.setOnClickListener {
            viewModel?.searchNow(current_search)
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, event  ->

            if (actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel?.searchNow(current_search)
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
            viewModel?.searchDebounce(current_search)


        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistoryGroup.isVisible =
                historyListModel?.size()!! > 0 && hasFocus && binding.inputEditText.text.isEmpty()
            historyAdapter.notifyDataSetChanged()
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Log.i("onSaveInstanceState", current_search)
        outState.putString(SEARCH_STRING, current_search)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        current_search = savedInstanceState.getString(SEARCH_STRING, STRING_DEF)
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
        showMessage(message, "", int)

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