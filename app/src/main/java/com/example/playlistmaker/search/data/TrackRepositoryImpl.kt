package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.db.domain.api.FavoritesTracksRepository
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


class TrackRepositoryImpl(private val networkClient: NetworkClient,
                          private val favoritesTracksRepository: FavoritesTracksRepository): TrackRepository {


    override fun searchTracks(expression: String): Flow<Resource<List<TrackData>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Сеть недоступна или произошла ошибка соединения", response.resultCode))
            }
            200 -> {
                val  tracks = (response as TrackSearchResponse)
                    .results
                    .map { it.toTrackData() }
                val favoriteIds = favoritesTracksRepository.getExistTracks(
                    tracksIds = tracks.map { it.trackId }
                )
                    .first()
                    .toHashSet()
                val result = tracks.map { track ->
                    track.copy(
                        isFavorite = track.trackId in favoriteIds
                    )
                }
                emit(  Resource.Success(result))
            }
            else -> {
                emit( Resource.Error("Неизвестная ошибка", 0))
            }
        }
    }
}