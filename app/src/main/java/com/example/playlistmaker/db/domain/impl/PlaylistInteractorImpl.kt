package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.db.domain.api.PlaylistRepository
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    val playlistRepository: PlaylistRepository,
    val saveFileInteractor: SaveFileInteractor
): PlaylistInteractor {


    override suspend fun create(playlist: PlaylistModel): PlaylistModel{
        return playlistRepository.create(playlist)
    }

    override suspend fun update(playlist: PlaylistModel): PlaylistModel {
        return playlistRepository.update(playlist).withImageUri()
    }
    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return playlistRepository.getPlaylists().map { playlists ->
            playlists.map { playlist ->
                playlist.withImageUri()
            }
        }
    }
    override fun getPlaylistById(id: Long):Flow<PlaylistModel?>{
        return playlistRepository.getPlaylistById(id).map {
            it?.withImageUri()
        }
    }

    override fun getTracks(playlistId: Long): Flow<List<TrackData>>{
        return playlistRepository.getTracks(playlistId)
    }
    override suspend fun getTrack(
        playlistId: Long,
        trackId: String,
    ): TrackData? {
        return playlistRepository.getTrack(
            playlistId = playlistId,
            trackId = trackId,
        )
    }

    override suspend fun isTrackInPlaylist(
        playlistId: Long,
        trackId: String,
    ): Boolean {
        return getTrack(
            playlistId = playlistId,
            trackId = trackId,
        ) != null
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: String) {
        playlistRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun addTrackToPlaylist(
        playlistId: Long,
        track: TrackData,
    ): Boolean {
        if (isTrackInPlaylist(playlistId = playlistId, trackId = track.trackId)) {
            return false
        }

        return playlistRepository.addTrackToPlaylist(playlistId = playlistId, track = track)
    }

    private fun PlaylistModel.withImageUri(): PlaylistModel {
        val uri = imageName
            ?.takeIf { it.isNotBlank() }
            ?.let { fileName ->
                saveFileInteractor.getFromInternalStorage(fileName)
            }

        return copy(imageUri = uri)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }
}