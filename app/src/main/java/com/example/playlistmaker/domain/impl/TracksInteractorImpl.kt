package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.TrackData
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TrackRepository) : TrackInteractor  {


    override fun searchTracks(
        expression: String,
        onSuccess: (List<TrackData>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        repository.searchTracks(expression, onSuccess , onFailure)
    }
}