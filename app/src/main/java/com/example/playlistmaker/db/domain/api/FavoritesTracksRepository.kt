package com.example.playlistmaker.db.domain.api



import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow

interface FavoritesTracksRepository {


    fun favoritesTracks(): Flow<List<TrackData>>

    suspend fun deleteFavoriteTrack(track : String)


    suspend fun insertFavoriteTrack(track : TrackData)

    suspend fun getExistFavoriteTrack(tracksId: String): TrackData?
}