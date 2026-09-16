package com.example.playlistmaker.db.data.entity

import androidx.room.Embedded

data class PlaylistWithTrackCount(
    @Embedded
    val playlist: PlaylistEntity,

    val trackCount: Int,
)