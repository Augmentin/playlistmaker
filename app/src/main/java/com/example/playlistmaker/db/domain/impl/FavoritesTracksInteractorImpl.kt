package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.api.FavoritesTracksInteractor
import com.example.playlistmaker.db.domain.api.FavoritesTracksRepository
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow

class FavoritesTracksInteractorImpl(
    private val favoritesTracksRepository: FavoritesTracksRepository
) : FavoritesTracksInteractor {

    override fun favoritesTracks(): Flow<List<TrackData>> {
        return favoritesTracksRepository.favoritesTracks()
    }

    override suspend fun deleteFavoriteTrack(track: TrackData) {
        favoritesTracksRepository.deleteFavoriteTrack(track.trackId)
    }

    override suspend fun insertFavoriteTrack(track : TrackData){
        favoritesTracksRepository.insertFavoriteTrack(track)
    }


    override suspend fun getExistFavoriteTrack(tracksId: String): TrackData? {
        return favoritesTracksRepository.getExistFavoriteTrack(tracksId)
    }
}