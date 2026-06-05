package com.example.playlistmaker.presentation.player

interface AudioPlayer {
    fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int

    fun playbackControl(onPaused: () -> Unit, onStart: () -> Unit)
}