package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlaylistBinding
import com.example.playlistmaker.search.domain.models.TrackData
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistActivity  : AppCompatActivity() {


    private lateinit var binding: ActivityPlaylistBinding
    private var url: String = ""

    private val  viewModel: PlayerViewModel by viewModel{
        parametersOf(url)
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())

    private val yearFormat by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlistMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.backToolbar.setNavigationOnClickListener {
            finish()
        }
        var json = intent.getStringExtra("TRACK")
        Log.i("Track", json ?: "")
        val track = Gson().fromJson(json, TrackData::class.java)


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
        if(track.previewUrl != null){
            url =  track.previewUrl;
        }
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

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


}