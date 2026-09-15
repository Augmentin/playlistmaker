package com.example.playlistmaker.player.ui.view_model

sealed interface TrackToPlaylistsEvent {

    data object Opened : TrackToPlaylistsEvent

    data class AlreadyAdded(
        val playlistName: String,
    ) : TrackToPlaylistsEvent

    data class AddedSuccess(
        val playlistName: String,
    ) : TrackToPlaylistsEvent
}