package com.example.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites_track_table")
data class TrackEntity (    @PrimaryKey
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
                            val created: Long = System.currentTimeMillis()
    )