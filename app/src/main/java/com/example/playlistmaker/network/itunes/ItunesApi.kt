package com.example.playlistmaker.network.itunes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {


    @GET("/Augmentin/playlistmaker/refs/heads/dev/jsons/itunes_1.json")
    fun search(@Query("term") text: String): Call<SearchResponse>

}