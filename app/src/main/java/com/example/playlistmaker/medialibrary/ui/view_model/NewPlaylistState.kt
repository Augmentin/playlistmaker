package com.example.playlistmaker.medialibrary.ui.view_model



sealed interface NewPlaylistState {
    object DisableSave : NewPlaylistState
    object EnableSave : NewPlaylistState

}