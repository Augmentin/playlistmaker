package com.example.playlistmaker.db.data.entity


import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTracksEntity(
    @Embedded
    val playlist: PlaylistEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlaylistTracks::class,
            parentColumn = "playlistId",
            entityColumn = "trackId",
        ),
    )
    val tracks: List<TrackEntity>,
)