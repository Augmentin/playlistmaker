package com.example.playlistmaker.medialibrary.ui.view_model


import com.example.playlistmaker.R.drawable.not_found
import com.example.playlistmaker.R.string.empty_favorites
import com.example.playlistmaker.search.domain.models.TrackData


sealed interface FavouritesTracksState {

    object Loading : FavouritesTracksState

    data class Content(
        val tracks: List<TrackData>
    ) : FavouritesTracksState

    object Error : FavouritesTracksState

    data class Empty(
        val message: Int = empty_favorites,
        val img: Int = not_found,
    )  : FavouritesTracksState
}