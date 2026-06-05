package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDataTdo
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.TrackData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {



    override fun searchTracks(expression: String,
                              onSuccess: (List<TrackData>) -> Unit,
                              onFailure: (Throwable) -> Unit
    ) {
       networkClient.loadTracks(expression,
             object : Callback<TrackSearchResponse> {
                override fun onResponse(
                    call: Call<TrackSearchResponse?>?,
                    response: Response<TrackSearchResponse?>?
                ) {
                    if (response?.isSuccessful == true) {
                        val results: MutableList<TrackDataTdo> =
                            response.body()?.results ?: mutableListOf()
                        val result = results.map {
                            it.toTrackData()
                        }
                        onSuccess(result)
                    }else{
                        onFailure(Exception("Fail to loading data"))
                    }
                }
                override fun onFailure(
                    call: Call<TrackSearchResponse>,
                    t: Throwable
                ) {
                    onFailure(t)
                }
            }
        )
    }
}