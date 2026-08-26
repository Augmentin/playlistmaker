package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.data.convertors.TrackDbConvertors
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.db.domain.api.FavoritesTracksRepository
import com.example.playlistmaker.search.data.toTrackDataEntity
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertors: TrackDbConvertors,
): FavoritesTracksRepository {

    override fun favoritesTracks(): Flow<List<TrackData>>  = flow {
        val tracks = appDatabase.movieDao().getTracks();
        emit(convertFromTracksEntity(tracks))
    }

    override suspend fun deleteFavoriteTrack(track: TrackData) {
        appDatabase.movieDao().deleteTrack(track.toTrackDataEntity())
    }

    override suspend fun insertTracks(list: List<TrackData>){
        appDatabase.movieDao().insertTracks(convertFromTracksData(list))
    }

    override fun getExistTracks(tracksIds: List<String>): Flow<List<String>>  = flow {
        val tracksIds = appDatabase.movieDao().getExistIds(tracksIds)
        emit(tracksIds)
    }

    private fun convertFromTracksData(tracks: List<TrackData>): List<TrackEntity>{
        return tracks.map { movie -> trackDbConvertors.map(movie) }
    }
    private fun convertFromTracksEntity(tracks: List<TrackEntity>): List<TrackData> {
        return tracks.map { movie -> trackDbConvertors.map(movie) }
    }
}