package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.NetworkConstants
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackDataTdo
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class ItunesClient : NetworkClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
    }


    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.ITUNES_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
                    .create()
            )
        )
        .build();

    val imdbService: ItunesApiService by lazy {
        retrofit.create(ItunesApiService::class.java)
    }

    override fun loadTracks( expression: String, callback: Callback<TrackSearchResponse>) {
        imdbService.search(expression).enqueue(callback)
    }


}