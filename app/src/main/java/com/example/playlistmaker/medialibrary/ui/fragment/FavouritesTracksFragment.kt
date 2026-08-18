package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import com.example.playlistmaker.databinding.FragmentMedialibraryTabBinding
import com.example.playlistmaker.medialibrary.ui.view_model.FavouritesModel
import com.example.playlistmaker.medialibrary.ui.view_model.FavouritesTracksState
import org.koin.androidx.viewmodel.ext.android.viewModel



class FavouritesTracksFragment: Fragment() {

    private val favouritesTracksModel: FavouritesModel by viewModel()
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

        favouritesTracksModel.observeState().observe(viewLifecycleOwner) {
            rander(it);
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun rander(state:FavouritesTracksState){
        when(state){
            is  FavouritesTracksState.Loading -> {}
            is  FavouritesTracksState.Content -> {}
            is FavouritesTracksState.Empty -> {
                showEmpty(state.message, state.img)
            }
            is FavouritesTracksState.Error -> {}
        }
    }

    fun showEmpty(massage: String, img: Int){
        binding.newPlaylist.isVisible = false
        binding.failImg.isVisible = true
        binding.placeholderTitle.isVisible = true
        binding.failImg.setImageResource(img)
        binding.placeholderTitle.text = massage
    }

    companion object {
        fun newInstance() = FavouritesTracksFragment()
    }
}