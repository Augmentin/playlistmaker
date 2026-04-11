package com.example.playlistmaker.network.itunes

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TrackData(
    val trackId: String,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?
)
