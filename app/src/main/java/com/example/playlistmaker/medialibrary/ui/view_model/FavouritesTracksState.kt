package com.example.playlistmaker.medialibrary.ui.view_model



import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.TrackData


sealed interface FavouritesTracksState {

    object Loading : FavouritesTracksState

    data class Content(
        val tracks: List<TrackData>
    ) : FavouritesTracksState

    object Error : FavouritesTracksState

    data class Empty(
        @StringRes val message: Int = R.string.empty_favorites,
        @DrawableRes val img: Int = R.drawable.not_found
    ) : FavouritesTracksState
}