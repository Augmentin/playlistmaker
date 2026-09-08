package com.example.playlistmaker.db.domain.api

import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel

interface PlaylistInteractor {

    suspend fun create(playlist: PlaylistModel): PlaylistModel
}