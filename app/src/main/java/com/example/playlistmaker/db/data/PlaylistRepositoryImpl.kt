package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.data.convertors.TrackDbConvertors
import com.example.playlistmaker.db.domain.api.PlaylistRepository
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertors: TrackDbConvertors,
): PlaylistRepository {

    override suspend fun create(playlist: PlaylistModel): PlaylistModel {
       val id =  appDatabase.playlistDao().insertPlaylist(trackDbConvertors.map(playlist))
       return playlist.copy(id = id)
    }
}