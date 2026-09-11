package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.FragmentMedialibraryTabBinding

import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.medialibrary.ui.activity.PlayListAdapter
import com.example.playlistmaker.medialibrary.ui.view_model.PlaylistsModel
import com.example.playlistmaker.medialibrary.ui.view_model.PlaylistsState

import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlayListFragment: Fragment() {

    private val playlistModel: PlaylistsModel by viewModel()
    private var _binding: FragmentMedialibraryTabBinding? = null
    private val binding get() = _binding!!


    private lateinit var playlistAdapter: PlayListAdapter
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
        initRecyclerView()
        playlistModel.observeState().observe(viewLifecycleOwner) {
            render(it);
        }
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlaylistFragment,
            )
        }
        observeCreatedPlaylist()
        playlistModel.update()
    }

    private fun initRecyclerView() {
        playlistAdapter = PlayListAdapter(
            onPlaylistClick ={ playlist ->
                openPlaylist(playlist)
            },
        )

        binding.songItems.apply {
            adapter = playlistAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        binding.songItems.isVisible = true
    }

    private fun observeCreatedPlaylist() {
        val savedStateHandle = findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?: return

        savedStateHandle
            .getLiveData<String>(
                CreatePlaylistFragment.CREATED_PLAYLIST_NAME_KEY
            )
            .observe(viewLifecycleOwner) { playlistName ->

                Toast.makeText(
                    requireContext(),
                    "Плейлист «$playlistName» создан",
                    Toast.LENGTH_SHORT,
                ).show()


               // playlistModel.update()

                savedStateHandle.remove<String>(
                    CreatePlaylistFragment.CREATED_PLAYLIST_NAME_KEY
                )
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun render(state: PlaylistsState){
        when(state){
            is  PlaylistsState.Loading -> {}
            is  PlaylistsState.Content -> {
                showContent(state.playlists)
            }
            is PlaylistsState.Empty -> {
                showEmpty(state.message, state.img)
            }
            is PlaylistsState.Error -> {}
        }
    }
    fun showContent(requestedTrackList: List<PlaylistModel>){
        binding.songItems.isVisible = true
        playlistAdapter.playlist.clear()
        playlistAdapter.playlist.addAll(requestedTrackList)
        playlistAdapter.notifyDataSetChanged()
    }
    fun showEmpty(massage: Int, img: Int){
        binding.songItems.isVisible = false
        binding.newPlaylist.isVisible = true
        binding.failImg.isVisible = true
        binding.placeholderTitle.isVisible = true
        binding.failImg.setImageResource(img)
        binding.placeholderTitle.text = getString(massage)
    }

    companion object {
        fun newInstance() = PlayListFragment()
    }

    private fun openPlaylist(playlist: PlaylistModel) {

    }
}