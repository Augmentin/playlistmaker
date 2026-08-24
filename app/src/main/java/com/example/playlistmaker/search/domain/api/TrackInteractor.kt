package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow


interface TrackInteractor {

    fun searchTracks(expression: String): Flow<Pair<List<TrackData>?, Int?>>


}