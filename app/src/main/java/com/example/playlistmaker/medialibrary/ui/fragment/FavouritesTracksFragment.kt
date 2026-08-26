package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.FragmentMedialibraryTabBinding
import com.example.playlistmaker.medialibrary.ui.view_model.FavouritesModel
import com.example.playlistmaker.medialibrary.ui.view_model.FavouritesTracksState
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.search.ui.activity.SearchFragment
import com.example.playlistmaker.search.ui.activity.SongListAdapter
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.util.debounce
import com.google.gson.Gson

import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class FavouritesTracksFragment: Fragment() {

    private val favouritesTracksModel: FavouritesModel by viewModel()
    private var _binding: FragmentMedialibraryTabBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SongListAdapter
    private lateinit var onTrackClickDebounce: (TrackData) -> Unit
    private val viewModel by viewModel<FavouritesModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMedialibraryTabBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.update()

        onTrackClickDebounce = debounce<TrackData>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false)
        { track ->
            findNavController().navigate(
                R.id.action_favouritesTracksFragment_to_playerFragment,
                PlayerFragment.createArgs(Gson().toJson(track))
            )
        }
        adapter = SongListAdapter { track ->
            onTrackClickDebounce(track)
        }


        binding.songItems.adapter = adapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onResume() {
        super.onResume()
       // viewModel.update()
    }
    fun showContent(requestedTrackList: List<TrackData>){
        binding.songItems.isVisible = true
        adapter.trackList.clear()
        adapter.trackList.addAll(requestedTrackList)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyForm() {
        binding.newPlaylist.isVisible = false
        binding.songItems.isVisible = false
        binding.placeholderTitle.isVisible = false
        binding.failImg.isVisible = false

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun render(state:FavouritesTracksState){
        showEmptyForm()
        when(state){
            is  FavouritesTracksState.Loading -> {}
            is  FavouritesTracksState.Content -> {
                showContent(state.tracks)
            }
            is FavouritesTracksState.Empty -> {
                showEmpty(getString(state.message), state.img)
            }
            is FavouritesTracksState.Error -> {}
        }
    }

    fun showEmpty(massage: String, img: Int){
        binding.failImg.isVisible = true
        binding.placeholderTitle.isVisible = true
        binding.failImg.setImageResource(img)
        binding.placeholderTitle.text = massage
    }

    companion object {
        fun newInstance() = FavouritesTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}