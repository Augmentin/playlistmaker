package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String,
        consumer: TrackInteractor.TracksConsumer
    ) {
        executor.execute{
            when(val response = repository.searchTracks(expression)){
                is Resource.Success -> {consumer.consume(response.data, null)}
                is Resource.Error -> { consumer.consume(null, response.code)}
            }
        }
    }


}