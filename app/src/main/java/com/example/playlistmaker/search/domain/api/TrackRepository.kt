package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun searchTracks(expression: String):Flow<Resource<List<TrackData>>>
}