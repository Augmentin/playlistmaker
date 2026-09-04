package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table ORDER BY id DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylist(playlistId: Long): PlaylistEntity?

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)
}