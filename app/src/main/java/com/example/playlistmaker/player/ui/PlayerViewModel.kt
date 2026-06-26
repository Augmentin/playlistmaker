package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val url: String) : ViewModel() {

    companion object {
        fun getFactory(trackUrl: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(trackUrl)
            }
        }
    }

    private val playerStateLiveData = MutableLiveData(PlayerState())
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData


    private val mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerStateLiveData.value?.status == PlayerStatus.PLAYING) {
            startTimerUpdate()
        }
    }

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }
    fun onPause() {
        pausePlayer()
    }
    fun onPlayButtonClicked() {
        when(playerStateLiveData.value?.status) {
            PlayerStatus.PLAYING -> pausePlayer()
            PlayerStatus.PREPARED, PlayerStatus.PAUSED -> startPlayer()
            else -> {pausePlayer()}
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            val o =  playerStateLiveData.value;
            o?.status = PlayerStatus.PREPARED
            playerStateLiveData.postValue(o)
        }
        mediaPlayer.setOnCompletionListener {
            val o =  playerStateLiveData.value;
            o?.status = PlayerStatus.PREPARED
            playerStateLiveData.postValue(o)
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        val o =  playerStateLiveData.value;
        o?.status = PlayerStatus.PLAYING
        playerStateLiveData.postValue(o)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        val o =  playerStateLiveData.value;
        o?.status = PlayerStatus.PAUSED
        playerStateLiveData.postValue(o)
    }

    private fun startTimerUpdate() {
        val o = playerStateLiveData.value
        o?.time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition);
        playerStateLiveData.postValue(o)
        handler.postDelayed(timerRunnable, 200)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        val o = playerStateLiveData.value
        o?.time ="00:00"
        playerStateLiveData.postValue(o)
    }
}