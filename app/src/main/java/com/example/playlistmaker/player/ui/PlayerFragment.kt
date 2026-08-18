package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding

import com.example.playlistmaker.search.domain.models.TrackData
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!


    companion object {

        private const val ARGS_TRACK = "TRACK"

        fun createArgs(track: String): Bundle =
            bundleOf(ARGS_TRACK to track)

    }

    private lateinit var trackData: TrackData

    private val  viewModel: PlayerViewModel by viewModel{
        parametersOf(trackData)
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())

    private val yearFormat by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }


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
        var json = requireArguments().getString(ARGS_TRACK) ?: ""
        Log.i("Track", json )
        val track = Gson().fromJson(json, TrackData::class.java)
        trackData = track

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

        viewModel.observePlayerState().observe(this) {
            when(it.status){
                PlayerStatus.PREPARED -> {
                    binding.playButton.isEnabled = true
                    binding.playButton.setImageResource(R.drawable.play_button)
                }
                PlayerStatus.PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.pause)
                }

                PlayerStatus.PAUSED ->{
                    binding.playButton.setImageResource(R.drawable.play_button)
                }

                else -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                }
            }
            binding.trackTime.text = it?.time ?: "00:00"
        }

        if(track?.previewUrl.isNullOrBlank()){
            binding.playButton.isEnabled = false
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        Glide.with(this).load(track?.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(
                RoundedCorners(
                    binding.image.context.resources.getDimensionPixelSize(R.dimen.search_radius_image)
                )
            )
            .into(binding.image)

    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}