package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface TrackRepository {

    fun searchTracks(expression: String,
                     onSuccess: (List<TrackData>) -> Unit,
                     onFailure: (Throwable) -> Unit)
}