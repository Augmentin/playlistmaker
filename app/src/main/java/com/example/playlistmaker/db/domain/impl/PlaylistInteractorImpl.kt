package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.db.domain.api.PlaylistRepository
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel

class PlaylistInteractorImpl(val playlistRepository: PlaylistRepository): PlaylistInteractor {


    override suspend fun create(playlist: PlaylistModel): PlaylistModel{
        return playlistRepository.create(playlist)
    }

}