package com.example.playlistmaker.medialibrary.domain.model

import android.net.Uri

data class PlaylistModel(
    val id: Long?,
    val name: String,
    val imageName: String?,
    val description: String?,
    val trackCount: Int = 0,
    val totalTracksTime: Long = 0,
    val imageUri: Uri? = null,
){
    val totalTracksTimeMinutes: Long
        get() = totalTracksTime / 60_000L
}