package com.example.playlistmaker.db.domain.api

import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow

interface FavoritesTracksRepository {


    fun favoritesTracks(): Flow<List<TrackData>>

    suspend fun deleteFavoriteTrack(track : TrackData)

    suspend fun insertTracks(list:List<TrackData>)

    fun getExistTracks(tracksIds: List<String>): Flow<List<String>>
}