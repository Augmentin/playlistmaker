package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackDataTdo
import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Callback


interface NetworkClient {

    fun loadTracks(expression: String, callback: Callback<TrackSearchResponse>)
}