package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {


    override fun searchTracks(expression: String ): Flow<Pair<List<TrackData>?, Int?>> {
        return repository.searchTracks(expression).map { response ->
            when(response){
                is Resource.Success -> {
                    Pair(response.data, null)
                }
                is Resource.Error -> {
                    Pair(null, response.code)
                }
            }
        }

    }


}