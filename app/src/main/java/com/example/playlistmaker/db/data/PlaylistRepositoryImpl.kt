package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.data.convertors.TrackDbConvertors
import com.example.playlistmaker.db.data.entity.PlaylistWithTrackCount
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.db.domain.api.PlaylistRepository
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertors: TrackDbConvertors,
): PlaylistRepository {

    override suspend fun create(playlist: PlaylistModel): PlaylistModel {
       val id =  appDatabase.playlistDao().insertPlaylist(trackDbConvertors.map(playlist))
       return playlist.copy(id = id)
    }

    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return appDatabase
            .playlistDao()
            .getPlaylists()
            .map { playlists ->
                playlists.map { playlist ->
                    trackDbConvertors.map(playlist)
                }
            }
    }

    private fun convertFromPlaylistsEntity(playlists: List<PlaylistWithTrackCount>): List<PlaylistModel> {
        return playlists.map { playlist -> trackDbConvertors.map(playlist) }
    }
}