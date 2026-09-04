package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.data.convertors.TrackDbConvertors
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.db.domain.api.FavoritesTracksRepository
import com.example.playlistmaker.search.data.toTrackData
import com.example.playlistmaker.search.data.toTrackDataEntity
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertors: TrackDbConvertors,
): FavoritesTracksRepository {

    override fun favoritesTracks(): Flow<List<TrackData>>  = flow {
        val tracks = appDatabase.movieDao().getFavoriteTracks();
        emit(convertFromTracksEntity(tracks))
    }

    override suspend fun deleteFavoriteTrack(track: String) {
        appDatabase.movieDao().removeTrackFromFavorites(track)
    }

    override suspend fun insertFavoriteTrack(track: TrackData) {
        appDatabase.movieDao().addTrackToFavorites(track.toTrackDataEntity(favorite=true))
    }

    override suspend fun getExistFavoriteTrack(tracksId: String): TrackData? {
       return appDatabase.movieDao().getExistFavoriteTrack(tracksId)?.toTrackData()
    }

    private fun convertFromTracksData(tracks: List<TrackData>): List<TrackEntity>{
        return tracks.map { movie -> trackDbConvertors.map(movie) }
    }
    private fun convertFromTracksEntity(tracks: List<TrackEntity>): List<TrackData> {
        return tracks.map { movie -> trackDbConvertors.map(movie) }
    }
}