package com.example.playlistmaker.playlist.ui.view_model

sealed interface PlaylistState {
    object OpenTracksList:PlaylistState
    object OpenMenu:PlaylistState
}