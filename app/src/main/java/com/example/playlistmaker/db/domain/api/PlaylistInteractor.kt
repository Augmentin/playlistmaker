package com.example.playlistmaker.db.domain.api

import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun create(playlist: PlaylistModel): PlaylistModel

    fun getPlaylists(): Flow<List<PlaylistModel>>

    suspend fun getTrack(
        playlistId: Long,
        trackId: String,
    ): TrackData?

    suspend fun isTrackInPlaylist(
        playlistId: Long,
        trackId: String,
    ): Boolean


    suspend fun addTrackToPlaylist(
        playlistId: Long,
        track: TrackData,
    ): Boolean
}