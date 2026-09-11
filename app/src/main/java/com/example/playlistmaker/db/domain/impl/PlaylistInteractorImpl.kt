package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.db.domain.api.PlaylistRepository
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    val playlistRepository: PlaylistRepository,
    val saveFileInteractor: SaveFileInteractor
): PlaylistInteractor {


    override suspend fun create(playlist: PlaylistModel): PlaylistModel{
        return playlistRepository.create(playlist)
    }
    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return playlistRepository.getPlaylists().map { playlists ->
            playlists.map { playlist ->
                playlist.withImageUri()
            }
        }
    }

    private fun PlaylistModel.withImageUri(): PlaylistModel {
        val uri = imageName
            ?.takeIf { it.isNotBlank() }
            ?.let { fileName ->
                saveFileInteractor.getFromInternalStorage(fileName)
            }

        return copy(imageUri = uri)
    }
}