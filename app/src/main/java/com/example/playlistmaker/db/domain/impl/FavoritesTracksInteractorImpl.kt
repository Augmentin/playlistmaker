package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.api.FavoritesTracksInteractor
import com.example.playlistmaker.db.domain.api.FavoritesTracksRepository
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class FavoritesTracksInteractorImpl(
    private val favoritesTracksRepository: FavoritesTracksRepository
) : FavoritesTracksInteractor {
    override fun favoritesTracks(): Flow<List<TrackData>> {
        return favoritesTracksRepository
            .favoritesTracks()
            .map { tracks ->
                tracks.map {
                    it.copy(isFavorite = true)
                }
            }
    }

    override suspend fun deleteFavoriteTrack(track: TrackData) {
        favoritesTracksRepository.deleteFavoriteTrack(track)
    }

    override suspend fun insertTracks(list: List<TrackData>) {
        favoritesTracksRepository.insertTracks(list)
    }

    override fun getExistTracks(tracksIds: List<String>): Flow<List<String>> {
        return favoritesTracksRepository.getExistTracks(tracksIds)
    }
}