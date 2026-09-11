package com.example.playlistmaker.medialibrary.domain.model

import android.net.Uri

data class PlaylistModel(
    val id: Long?,
    val name: String,
    val imageName: String?,
    val description: String?,
    val trackCount: Int = 0,
    val imageUri: Uri? = null,
) {

    fun getTrackCountText(): String {
        val word = when {
            trackCount % 100 in 11..14 -> "треков"
            trackCount % 10 == 1 -> "трек"
            trackCount % 10 in 2..4 -> "трека"
            else -> "треков"
        }

        return "$trackCount $word"
    }
}