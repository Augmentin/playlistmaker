package com.example.playlistmaker.search.data

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {


    override fun searchTracks(expression: String): Flow<Resource<List<TrackData>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Сеть недоступна или произошла ошибка соединения", response.resultCode))
            }
            200 -> {
                emit( Resource.Success((response as TrackSearchResponse).results.map {
                    it.toTrackData()
                }))
            }
            else -> {
                emit( Resource.Error("Неизвестная ошибка", 0))
            }
        }
    }
}