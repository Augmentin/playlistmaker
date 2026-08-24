package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val trackData: TrackData,  private val mediaPlayer: MediaPlayer) : ViewModel() {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    init {
        initMediaPlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
        // resetTimer()
    }

    fun onPause() {
        pausePlayer()
    }
    fun onPlayButtonClicked() {
        when(playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }
            else -> { }
        }
    }

    private fun initMediaPlayer() {

        Log.d("PLAYER", "trackData = '$trackData'")
        if(!trackData.previewUrl.isNullOrBlank()){
            mediaPlayer.setDataSource(trackData.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState.postValue(PlayerState.Prepared())
            }
            mediaPlayer.setOnCompletionListener {
                playerState.postValue(PlayerState.Prepared())
            }
        }

    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }
    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.value = PlayerState.Default()
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }

}