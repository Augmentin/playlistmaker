package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.search.domain.models.TrackData

interface TrackRepository {

    fun searchTracks(expression: String): Resource<List<TrackData>>
}