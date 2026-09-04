package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.FragmentMedialibraryTabBinding
import com.example.playlistmaker.medialibrary.ui.view_model.PlayListModel
import com.example.playlistmaker.medialibrary.ui.view_model.PlayListState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlayListFragment: Fragment() {

    private val playlistModel: PlayListModel by viewModel()
    private var _binding: FragmentMedialibraryTabBinding? = null
    private val binding get() = _binding!!

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

        playlistModel.observeState().observe(viewLifecycleOwner) {
            rander(it);
        }
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlaylistFragment,
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun rander(state: PlayListState){
        when(state){
            is  PlayListState.Loading -> {}
            is  PlayListState.Content -> {}
            is PlayListState.Empty -> {
                showEmpty(state.message, state.img)
            }
            is PlayListState.Error -> {}
        }
    }

    fun showEmpty(massage: String, img: Int){
        binding.songItems.isVisible = false
        binding.newPlaylist.isVisible = true
        binding.failImg.isVisible = true
        binding.placeholderTitle.isVisible = true
        binding.failImg.setImageResource(img)
        binding.placeholderTitle.text = massage
    }

    companion object {
        fun newInstance() = PlayListFragment()
    }
}