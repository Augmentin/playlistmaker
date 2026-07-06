package com.example.playlistmaker.medialibrary.ui.view_model



sealed interface  PlayListState {

    object Loading : PlayListState

    object Content : PlayListState

    object Error : PlayListState

    data class Empty(
        val message: String,
        val img: Int,
    ) : PlayListState
}