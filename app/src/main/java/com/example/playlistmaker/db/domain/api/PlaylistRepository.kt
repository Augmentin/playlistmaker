package com.example.playlistmaker.db.domain.api

import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel

interface PlaylistRepository {

    suspend fun create(playlist: PlaylistModel): PlaylistModel
}