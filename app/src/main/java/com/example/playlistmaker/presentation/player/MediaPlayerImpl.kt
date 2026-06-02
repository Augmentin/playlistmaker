package com.example.playlistmaker.presentation.player

import android.media.MediaPlayer

class MediaPlayerImpl: AudioPlayer {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    override fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState =  PlayerState.PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            playerState = PlayerState.PREPARED
            onCompletion()
        }
    }

    override fun play(){
        playerState = PlayerState.PLAYING
        mediaPlayer.start()
    }

    override fun pause(){
        playerState = PlayerState.PAUSED
        mediaPlayer.pause()
    }


    override fun release() = mediaPlayer.release()

    override fun getCurrentPosition() = mediaPlayer.currentPosition

    override fun playbackControl(onPaused: () -> Unit, onStart: () -> Unit) {
        when (playerState) {
            PlayerState.PLAYING -> {
                onPaused()
                pause()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                onStart()
                play()
            }
            else -> {}
        }
    }
}