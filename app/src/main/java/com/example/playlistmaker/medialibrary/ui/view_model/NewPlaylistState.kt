package com.example.playlistmaker.medialibrary.ui.view_model



sealed interface NewPlaylistState {
    object EmptyRequiredFields : NewPlaylistState
    object FilledRequiredFields : NewPlaylistState
}