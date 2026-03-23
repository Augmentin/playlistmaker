package com.example.playlistmaker.network.itunes

data class SearchResponse(val resultCount: Int, val results: ArrayList<TrackData>)