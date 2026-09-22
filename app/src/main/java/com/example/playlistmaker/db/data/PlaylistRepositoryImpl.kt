package com.example.playlistmaker.db.data

import androidx.room.withTransaction
import com.example.playlistmaker.db.data.convertors.TrackDbConvertors
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.data.entity.PlaylistTracks
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

    override fun getPlaylistById(id: Long):Flow<PlaylistModel?>{
        return appDatabase.playlistDao().getPlaylistById(id).map { it?.let { trackDbConvertors.map(it) } }
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: String) {
        appDatabase.playlistDao().deleteTrackFromPlaylist(playlistId, trackId)

    }

    override fun getTracks(playlistId: Long): Flow<List<TrackData>>{
        return appDatabase
            .playlistDao()
            .getTracks(playlistId).map { tracks ->
                tracks.map { track ->
                trackDbConvertors.map(track)
            }
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDatabase.withTransaction {
            appDatabase.playlistDao().deleteUnusedPlaylistTracks(playlistId)
            appDatabase.playlistDao().deletePlaylist(playlistId)
        }
    }

    override suspend fun getTrack(
        playlistId: Long,
        trackId: String,
    ): TrackData? {
        val trackEntity = appDatabase
            .playlistDao()
            .getTrack(
                playlistId = playlistId,
                trackId = trackId,
            )

        return trackEntity?.let { entity ->
            trackDbConvertors.map(entity)
        }
    }

    override suspend fun addTrackToPlaylist(
        playlistId: Long,
        track: TrackData,
    ): Boolean {
        return appDatabase.withTransaction {
            appDatabase.trackDao().insertTrackIfAbsent(trackDbConvertors.map(track))
            val insertedRowId = appDatabase.playlistDao().insertPlaylistTrack(PlaylistTracks(
                        playlistId = playlistId,
                        trackId = track.trackId))

            insertedRowId != -1L
        }
    }
    override suspend fun update(playlist: PlaylistModel): PlaylistModel {
        appDatabase.playlistDao().updatePlaylist(trackDbConvertors.map(playlist))
        return playlist
    }



    private fun convertFromPlaylistsEntity(playlists: List<PlaylistWithTrackCount>): List<PlaylistModel> {
        return playlists.map { playlist -> trackDbConvertors.map(playlist) }
    }
}