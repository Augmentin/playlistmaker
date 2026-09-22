package com.example.playlistmaker.medialibrary.ui.view_model

sealed interface SavePlayListState {

    object Editing : SavePlayListState

    data class Saved(
        val playlistId: Long,
        val playlistName: String,
    ) : SavePlayListState

    data class Error(
        val message: String,
    ) : SavePlayListState
}