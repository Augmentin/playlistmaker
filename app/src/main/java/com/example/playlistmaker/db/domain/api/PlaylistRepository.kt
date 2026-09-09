package com.example.playlistmaker.db.domain.api

import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun create(playlist: PlaylistModel): PlaylistModel

    fun getPlaylists(): Flow<List<PlaylistModel>>
}