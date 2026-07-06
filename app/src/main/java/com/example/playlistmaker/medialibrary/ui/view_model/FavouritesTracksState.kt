package com.example.playlistmaker.medialibrary.ui.view_model

sealed interface   FavouritesTracksState {

    object Loading : FavouritesTracksState

    object Content : FavouritesTracksState

    object Error : FavouritesTracksState

    data class Empty(
        val message: String,
        val img: Int,
    )  : FavouritesTracksState
}