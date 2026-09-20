package com.example.playlistmaker.db.domain.api


import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun create(playlist: PlaylistModel): PlaylistModel

    fun getPlaylists(): Flow<List<PlaylistModel>>
    fun getTracks(playlistId: Long): Flow<List<TrackData>>

    fun getPlaylistById(id: Long):Flow<PlaylistModel?>

    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: String)

    suspend fun addTrackToPlaylist(
        playlistId: Long,
        track: TrackData,
    ): Boolean
    suspend fun getTrack(
        playlistId: Long,
        trackId: String,
    ): TrackData?
}