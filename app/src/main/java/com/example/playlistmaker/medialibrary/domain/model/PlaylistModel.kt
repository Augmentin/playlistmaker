package com.example.playlistmaker.medialibrary.domain.model

data class PlaylistModel(
    val id: Long?,
    val name: String,
    val imageName: String?,
    val description: String?,
    val trackCount: Int = 0,
) {
}