package com.example.playlistmaker.db.data.entity

import androidx.room.Embedded

data class PlaylistWithDetails (
    @Embedded
    val playlist: PlaylistEntity,
    val trackCount: Int,
    val totalTracksTime: Long,
)