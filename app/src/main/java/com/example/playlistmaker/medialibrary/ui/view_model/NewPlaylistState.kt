package com.example.playlistmaker.medialibrary.ui.view_model



sealed interface NewPlaylistState {
    object EmptyRequiredFields : NewPlaylistState
    object FilledRequiredFields : NewPlaylistState


    object Saving : NewPlaylistState

    data class Saved(
        val playlistId: Long,
        val playlistName: String,
    ) : NewPlaylistState

    data class Error(
        val message: String,
    ) : NewPlaylistState
}