package com.example.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.medialibrary.ui.fragment.EditPlaylistFragment
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.player.ui.activity.PlayListAdapter
import com.example.playlistmaker.playlist.ui.view_model.PlaylistState

import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.search.ui.activity.SongListAdapter
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN


import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlaylistFragment : Fragment() {

    companion object {

        private const val ARGS_PLAYLIST_ID = "PLAYLIST_ID"
        private const val CLICK_DEBOUNCE_DELAY = 200L
        fun createArgs(playlistId: Long): Bundle {
            return bundleOf(ARGS_PLAYLIST_ID to playlistId)
        }

    }

    private lateinit var onTrackClickDebounce: (TrackData) -> Unit
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetMenu: BottomSheetBehavior<LinearLayout>
    private val  playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf(requireArguments().getLong(ARGS_PLAYLIST_ID))
    }
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var adapter: SongListAdapter
    private lateinit var playlistAdapter: PlayListAdapter
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlist) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backToolbar.setOnClickListener {
            findNavController().navigateUp()
        }

        playlistViewModel.observePlaylistLiveData().observe(viewLifecycleOwner) {
            renderPlaylist(it);
        }
        playlistViewModel.observeState().observe(viewLifecycleOwner){
            renderState(it)
        }
        onTrackClickDebounce = debounce<TrackData>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false)
        { track ->
            findNavController().navigate(
                R.id.action_playlistFragment_to_playerFragment,
                PlayerFragment.createArgs(Gson().toJson(track))
            )
        }

        playlistViewModel.observeTracksState().observe(viewLifecycleOwner){
            showTracks(it)
        }
        bottomSheetBehavior = from(binding.playlistsBottomSheet).apply {
            state = STATE_COLLAPSED
        }
        bottomSheetMenu = from(binding.playlistsBottomMenu).apply {
            state = STATE_HIDDEN
        }

        adapter = SongListAdapter { track ->
            onTrackClickDebounce(track)
        }

        adapter.setOnLongClickListener {
            track ->
            val confirmDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_track_message)
                .setNegativeButton(R.string.no) { dialog, which ->
                }.setPositiveButton(R.string.yes) { dialog, which ->
                    playlistViewModel.deleteTrack(track)
                }
            confirmDialog.show()
        }
        binding.songItems.layoutManager = LinearLayoutManager(requireContext())
        binding.songItems.adapter = adapter

        binding.btShare.setOnClickListener {
            share()
        }
        binding.more.setOnClickListener {
            playlistViewModel.openMenu()
        }

        bottomSheetMenu.addBottomSheetCallback(object : BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_HIDDEN -> {
                        playlistViewModel.hideMenu()
                    }
                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        initPlayerRecyclerView()
        initMenuButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initMenuButtons(){
        binding.shareButton.setOnClickListener {
            share()
        }
        binding.editButton.setOnClickListener {
            val playlist = playlistViewModel.getPlaylist()
            playlist?.id?.let {
                findNavController().navigate(
                    R.id.action_playlistFragment_to_editPlaylistFragment,
                    EditPlaylistFragment.createArgs(it)
                )
            }
        }
        binding.deletePlaylistButton.setOnClickListener {
            val playlist = playlistViewModel.getPlaylist()
            playlist?.let {
                playlist ->
                val confirmDialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.delete_playlist_message, playlist.name))
                    .setNegativeButton(R.string.no) { dialog, which ->
                    }.setPositiveButton(R.string.yes) { dialog, which ->
                        playlist?.id?.let {
                            playlistViewModel.deletePlaylist(it)
                            findNavController().popBackStack(
                                R.id.mediaLibraryFragment,
                                false
                            )
                        }
                    }
                confirmDialog.show()
            }

        }
    }

    private fun share(){
        if(playlistViewModel.getTracks().size > 0){
            val playlist = playlistViewModel.getPlaylist()
            playlist?.let {
                playlistViewModel.share(createPlaylistShareText(it, playlistViewModel.getTracks()))
            }
        }else{
            Toast.makeText(requireContext(), R.string.empty_playlist_share, Toast.LENGTH_SHORT).show()
        }
    }
    private fun initPlayerRecyclerView() {
        playlistAdapter = PlayListAdapter(
            onPlaylistClick ={ playlist -> }
        )

        binding.playlistItem.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.playlistItem.isVisible = true
    }
    private fun renderState(state: PlaylistState){
        when(state){
            is PlaylistState.OpenTracksList -> {
                bottomSheetMenu.state = STATE_HIDDEN
                binding.songItems.isEnabled = false
            }
            is PlaylistState.OpenMenu -> {
                bottomSheetMenu.state = STATE_COLLAPSED
                binding.songItems.isEnabled = true
            }
        }

    }
    private fun createPlaylistShareText(playlist: PlaylistModel, tracks: List<TrackData>): String {
        val trackCountText = resources.getQuantityString(
            R.plurals.playlist_track_count,
            tracks.size,
            tracks.size,
        )

        val tracksText = tracks.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - " +
                    "${track.trackName} (${track.trackTimeMillis?.let {
                        dateFormat.format(it)
                    } ?: "00:00"})"
        }.joinToString(separator = "\n")

        return buildString {
            appendLine(playlist.name)
            if (!playlist.description.isNullOrBlank()) {
                appendLine(playlist.description)
            }
            appendLine(trackCountText)
            append(tracksText)
        }
    }
    fun showTracks(requestedTrackList: List<TrackData>){
        binding.songItems.isVisible = true
        adapter.trackList.clear()
        adapter.trackList.addAll(requestedTrackList)
        adapter.notifyDataSetChanged()
    }
    fun renderPlaylist(playlist: PlaylistModel){
        playlistAdapter.playlist.clear()
        playlistAdapter.playlist.addAll(listOf(playlist))
        playlistAdapter.notifyDataSetChanged()
        binding.playlistName.text = playlist.name.trim()
        binding.playlistDescription.text = playlist.description?.trim()
        binding.trackCount.text = binding.root.resources.getQuantityString(
            R.plurals.playlist_track_count,
            playlist.trackCount,
            playlist.trackCount,
        )
        binding.totalTracksTime.text = binding.root.resources.getQuantityString(
            R.plurals.minutes_count,
            playlist.totalTracksTimeMinutes.toInt(),
            playlist.totalTracksTimeMinutes,
        )

        binding.image.apply {
            setBackgroundResource(
                if (playlist.imageUri == null) {
                    R.color.light_grey
                } else {
                    R.color.white
                },
            )
        }

        Glide.with(binding.image)
            .load(playlist.imageUri)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(binding.image)
    }
}