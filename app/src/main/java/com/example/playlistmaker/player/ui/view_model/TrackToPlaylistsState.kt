package com.example.playlistmaker.player.ui.view_model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel


sealed interface  TrackToPlaylistsState {

    object Loading : TrackToPlaylistsState

    data class Content(
        val playlists: List<PlaylistModel>
    ) : TrackToPlaylistsState

    object Error : TrackToPlaylistsState

    data class Empty(
        @StringRes val message: Int = R.string.empty_playlist,
        @DrawableRes val img: Int = R.drawable.not_found,
    ) : TrackToPlaylistsState

}