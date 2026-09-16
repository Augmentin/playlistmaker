package com.example.playlistmaker.db.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "tracks",
    indices = [
        Index(value = ["favorite"]),
    ],
)
data class TrackEntity(
    @PrimaryKey
    val id: String,

    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,

    @ColumnInfo(defaultValue = "0")
    val favorite: Boolean = false,

    val created: Long = System.currentTimeMillis(),
)