package com.example.playlistmaker.medialibrary.ui.view_model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel


sealed interface  PlaylistsState {

    object Loading : PlaylistsState

    data class Content(
        val tracks: List<PlaylistModel>
    ) : PlaylistsState

    object Error : PlaylistsState

    data class Empty(
        @StringRes val message: Int = R.string.empty_playlist,
        @DrawableRes val img: Int = R.drawable.not_found,
    ) : PlaylistsState
}