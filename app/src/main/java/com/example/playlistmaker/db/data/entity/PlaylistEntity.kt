package com.example.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val imageName: String?,
    val description: String?,
)