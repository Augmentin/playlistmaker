package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.data.entity.PlaylistTracks
import com.example.playlistmaker.db.data.entity.PlaylistWithDetails
import com.example.playlistmaker.db.data.entity.PlaylistWithTrackCount
import com.example.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query(
        """
    SELECT tracks.*
    FROM tracks
    INNER JOIN playlist_tracks
        ON tracks.id = playlist_tracks.trackId
    WHERE playlist_tracks.playlistId = :playlistId
      AND playlist_tracks.trackId = :trackId
    LIMIT 1
    """
    )
    suspend fun getTrack(
        playlistId: Long,
        trackId: String,
    ): TrackEntity?

    @Query(
        """
    SELECT tracks.*
    FROM tracks
    INNER JOIN playlist_tracks
        ON tracks.id = playlist_tracks.trackId
    WHERE playlist_tracks.playlistId = :playlistId
    """
    )
    fun getTracks( playlistId: Long): Flow<List<TrackEntity>>

    @Query(
        """
    SELECT 
        playlist_table.*,
        COUNT(playlist_tracks.trackId) AS trackCount
    FROM playlist_table
    LEFT JOIN playlist_tracks
        ON playlist_table.id = playlist_tracks.playlistId
    GROUP BY playlist_table.id
    ORDER BY playlist_table.id DESC
    """
    )
    fun getPlaylists(): Flow<List<PlaylistWithTrackCount>>


    @Query(
        """
    SELECT
        p.*,
        COUNT(pt.trackId) AS trackCount,
        COALESCE(SUM(t.trackTimeMillis), 0) AS totalTracksTime
    FROM playlist_table AS p
    LEFT JOIN playlist_tracks AS pt
        ON p.id = pt.playlistId
    LEFT JOIN tracks AS t
        ON pt.trackId = t.id
    WHERE p.id = :playlistId
    GROUP BY p.id
    """
    )
    fun getPlaylistById(
        playlistId: Long,
    ): Flow<PlaylistWithDetails?>


    @Query(
        """
    DELETE FROM playlist_tracks
    WHERE playlistId = :playlistId AND trackId = :trackId
    """
    )
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: String): Int

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(
        playlistTrack: PlaylistTracks,
    ): Long

}