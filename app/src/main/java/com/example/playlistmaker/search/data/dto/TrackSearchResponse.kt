package com.example.playlistmaker.search.data.dto
data class TrackSearchResponse(
    val resultCount: Int,
    val results: MutableList<TrackDataTdo>,
    val expression: String
) : Response()