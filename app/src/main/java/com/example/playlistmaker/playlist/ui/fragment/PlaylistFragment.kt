package com.example.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentPlaylistBinding

class PlaylistFragment : Fragment() {

    companion object {

        private const val ARGS_TRACK = "PLAYLIST_ARG"

        fun createArgs(track: String): Bundle =
            bundleOf(ARGS_TRACK to track)

    }
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToolbar.setOnClickListener {
            findNavController().navigateUp()
        }

    }

}