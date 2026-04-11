package com.example.playlistmaker.network.itunes

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TrackData(
    val trackId: String,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
){
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

}
