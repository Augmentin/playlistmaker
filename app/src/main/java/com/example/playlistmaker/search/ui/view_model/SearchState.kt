package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.search.domain.models.TrackData


sealed interface SearchState {
    object Loading : SearchState

    data class Content(
        val tracks: List<TrackData>
    ) : SearchState

    data class Error(
        val errorMessage: String,
        val img: Int,
        val errorPlaceholderMessage: String? = null
    ) : SearchState

    data class Empty(
        val message: String,
        val img: Int,
    ) : SearchState
}