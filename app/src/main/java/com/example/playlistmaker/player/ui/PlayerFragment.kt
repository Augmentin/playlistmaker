package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.medialibrary.ui.fragment.CreatePlaylistFragment
import com.example.playlistmaker.player.ui.activity.PlayListAdapter

import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.TrackToPlaylistModel
import com.example.playlistmaker.player.ui.view_model.TrackToPlaylistsEvent
import com.example.playlistmaker.player.ui.view_model.TrackToPlaylistsState

import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistAdapter: PlayListAdapter
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 200L
        private const val ARGS_TRACK = "TRACK"

        fun createArgs(track: String): Bundle =
            bundleOf(ARGS_TRACK to track)

    }

    private val trackData: TrackData by lazy{
        Gson().fromJson(
            requireArguments().getString(ARGS_TRACK),
            TrackData::class.java,
        )
    }
    private val trackToPlaylistModel: TrackToPlaylistModel by viewModel{
        parametersOf(trackData)
    }
    private val  playerViewModel: PlayerViewModel by viewModel{
        parametersOf(trackData)
    }
    private lateinit var onTrackClickDebounce: (PlaylistModel) -> Unit
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())

    private val yearFormat by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlistMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val track = trackData
        binding.trackTitle.text = track.trackName?.trim() ?: "Undefined"
        binding.trackArtist.text = track.artistName?.trim() ?: "Undefined"

        binding.durationValue.text = track.trackTimeMillis?.let {
            dateFormat.format(it)
        } ?: "00:00"

        if(track.collectionName.isNullOrEmpty()){
            binding.albumValue.isVisible =  false
            binding.album.isVisible =  false
        }else{
            binding.albumValue.text = track.collectionName
        }

        if(track.releaseDate.isNullOrEmpty()){
            binding.yearValue.isVisible = false
            binding.year.isVisible = false
        }else{
            binding.yearValue.text =  track.releaseDate.let {
                val date = inputFormat.parse(it)
                date?.let { d -> yearFormat.format(d) }
            }
        }

        binding.genreValue.text = track.primaryGenreName

        binding.countryValue.text = track.country

        playerViewModel.observePlayerState().observe(viewLifecycleOwner) {
            binding.playButton.setImageResource(it.buttonImage)
            binding.playButton.isEnabled =  it.isPlayButtonEnabled
            binding.trackTime.text = it.progress
        }
        playerViewModel.observeFavoriteState().observe(viewLifecycleOwner){
            if(it){
                binding.likeButton.setImageResource(R.drawable.active_like)
            }else{
                binding.likeButton.setImageResource(R.drawable.unactive_like)
            }
        }
        if(track?.previewUrl.isNullOrBlank()){
            binding.playButton.isEnabled = false
        }

        binding.playButton.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }
        binding.likeButton.setOnClickListener {
            playerViewModel.onClickLike()
        }
        Glide.with(this).load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(
                RoundedCorners(
                    binding.image.context.resources.getDimensionPixelSize(R.dimen.search_radius_image)
                )
            )
            .into(binding.image)

        onTrackClickDebounce = debounce<PlaylistModel>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope,
            false){
            playlistModel ->
            trackToPlaylistModel.addTrack(playlistModel)

        }
        bottomSheetBehavior = from(binding.playlistsBottomSheet).apply {
            state = STATE_HIDDEN
        }
        binding.addButton.setOnClickListener {
            trackToPlaylistModel.openMenu()
        }

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_createPlaylistFragment,
            )
        }
        trackToPlaylistModel.observeState().observe(viewLifecycleOwner) {
            render(it);
        }
        trackToPlaylistModel.observeEvent().observe(viewLifecycleOwner) { event ->
            handleEvent(event)
        }

        initRecyclerView()
        observeCreatedPlaylist()

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })


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




                savedStateHandle.remove<String>(
                    CreatePlaylistFragment.CREATED_PLAYLIST_NAME_KEY
                )
            }
    }
    private fun initRecyclerView() {
        playlistAdapter = PlayListAdapter(
            onPlaylistClick ={ playlist ->
                onTrackClickDebounce(playlist)
            },
        )

        binding.songItems.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.songItems.isVisible = true
    }

    fun handleEvent(event: TrackToPlaylistsEvent){
        when (event) {
            TrackToPlaylistsEvent.Opened -> {
                bottomSheetBehavior.state = STATE_COLLAPSED
            }
            is TrackToPlaylistsEvent.AlreadyAdded -> {
                Toast.makeText(
                    requireContext(),
                    "Трек уже добавлен в плейлист «${event.playlistName}»",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            is TrackToPlaylistsEvent.AddedSuccess -> {
                Toast.makeText(
                    requireContext(),
                    "Добавлено в плейлист «${event.playlistName}»",
                    Toast.LENGTH_SHORT,
                ).show()

                bottomSheetBehavior.state = STATE_HIDDEN
            }
        }
    }
    fun render(state: TrackToPlaylistsState){
        when(state){
            is  TrackToPlaylistsState.Loading -> {}
            is  TrackToPlaylistsState.Content -> {
                showContent(state.playlists)
            }
            is TrackToPlaylistsState.Empty -> {
                showEmpty(state.message, state.img)
            }
            is TrackToPlaylistsState.Error -> {}
        }
    }

    fun showContent(requestedTrackList: List<PlaylistModel>){
        binding.songItems.isVisible = true
        binding.failImg.isVisible = false
        binding.placeholderTitle.isVisible = false
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


    override fun onPause() {
        super.onPause()
        playerViewModel.onPause()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}