package com.example.playlistmaker.search.data

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.TrackData


class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {


    override fun searchTracks(expression: String): Resource<List<TrackData>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Сеть недоступна или произошла ошибка соединения", response.resultCode)
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    it.toTrackData()
                })
            }
            else -> {
                Resource.Error("Неизвестная ошибка", 0)
            }
        }
    }
}