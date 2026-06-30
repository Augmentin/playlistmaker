package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TrackData


interface TrackInteractor {

    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundMovies: List<TrackData>?, errorCode: Int?)
    }
}