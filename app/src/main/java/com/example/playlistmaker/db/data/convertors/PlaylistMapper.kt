package com.example.playlistmaker.db.data.convertors

import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.data.entity.PlaylistWithTrackCount
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel


fun PlaylistModel.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = id ?: 0,
        name = name,
        imageName = imageName,
        description = description,
    )
}

fun PlaylistEntity.toPlaylistModel(): PlaylistModel {
    return PlaylistModel(
        id = id,
        name = name,
        imageName = imageName,
        description = description,
    )
}

fun PlaylistWithTrackCount.toPlaylistModel(): PlaylistModel {
    return PlaylistModel(
        id = playlist.id,
        name = playlist.name,
        imageName = playlist.imageName,
        description = playlist.description,
        trackCount = trackCount,
    )
}