package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.data.entity.PlaylistWithTrackCount
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
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
    fun getPlaylists(): List<PlaylistWithTrackCount>
    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylist(playlistId: Long): PlaylistEntity?

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)


}