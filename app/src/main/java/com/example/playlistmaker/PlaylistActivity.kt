package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.network.itunes.TrackData
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistActivity  : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_PING_DELAY = 300L
    }

    private lateinit var playButton: ImageButton

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val playTimeRunnable = Runnable { playTime() }
    private lateinit var trackTime : TextView
    private lateinit var durationValue:TextView
    private var playerState = STATE_DEFAULT
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())

    val format =  SimpleDateFormat("mm:ss", Locale.getDefault())
    private val yearFormat by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_playlist)
        val currentView = findViewById<View>(R.id.playlistMain);
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playButton = findViewById(R.id.playButton)
        trackTime = findViewById(R.id.trackTime)
        val back = findViewById<MaterialToolbar>(R.id.back_toolbar)
        back.setNavigationOnClickListener {
            finish()
        }
        var json = intent.getStringExtra("TRACK")
        Log.i("Track", json ?: "")
        val track = Gson().fromJson(json, TrackData::class.java)

        val trackTitle = findViewById<TextView>(R.id.trackTitle)
        val trackArtist = findViewById<TextView>(R.id.trackArtist)
        trackTitle.text = track.trackName?.trim() ?: "Undefined"
        trackArtist.text = track.artistName?.trim() ?: "Undefined"

        durationValue = findViewById(R.id.durationValue)
        durationValue.text = track.trackTimeMillis?.let {
            dateFormat.format(it)
        } ?: "00:00"
        val albumValue = findViewById<TextView>(R.id.albumValue)

        if(track.collectionName.isNullOrEmpty()){
            albumValue.isVisible =  false
            val album = findViewById<TextView>(R.id.album)
            album.isVisible =  false
        }else{
            albumValue.text = track.collectionName
        }
        val yearValue = findViewById<TextView>(R.id.yearValue)
        if(track.releaseDate.isNullOrEmpty()){
            yearValue.isVisible = false
            val year = findViewById<TextView>(R.id.year)
            year.isVisible = false
        }else{
            yearValue.text =  track.releaseDate.let {
                val date = inputFormat.parse(it)
                date?.let { d -> yearFormat.format(d) }
            }
        }
        val genreValue = findViewById<TextView>(R.id.genreValue)
        genreValue.text = track.primaryGenreName
        val countryValue = findViewById<TextView>(R.id.countryValue)
        countryValue.text = track.country
        val image = findViewById<ImageView>(R.id.image)
        preparePlayer(track?.previewUrl )

        playButton.setOnClickListener {
            playbackControl()
        }
        Glide.with(this).load(track?.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(
                RoundedCorners(
                    image.context.resources.getDimensionPixelSize(R.dimen.search_radius_image)
                )
            )
            .into(image)
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    private fun playTime(){
       Log.i("mediapleer",  format.format(mediaPlayer.currentPosition))
       trackTime.text = format.format(mediaPlayer.currentPosition)
       handler.postDelayed(playTimeRunnable, TIME_PING_DELAY)
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(playTimeRunnable)
        mediaPlayer.release()
    }


    private fun preparePlayer(url: String?) {
        if(url.isNullOrEmpty()){
            playButton.isEnabled = false
        }else{
            playButton.isEnabled = true
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playButton.isEnabled = true
                playButton.setImageResource(R.drawable.play_button)
                playerState =  STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                handler.removeCallbacks(playTimeRunnable)
                playButton.setImageResource(R.drawable.play_button)
                playerState = STATE_PREPARED
                trackTime.text = "00:00"
            }
        }

    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        playerState =STATE_PLAYING
        playTime()
    }

    private fun pausePlayer() {
        handler.removeCallbacks(playTimeRunnable)
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

}