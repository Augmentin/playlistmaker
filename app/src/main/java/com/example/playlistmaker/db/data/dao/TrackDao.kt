package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    @Query("DELETE FROM favorites_track_table WHERE id = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("SELECT * FROM favorites_track_table ORDER BY created DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM favorites_track_table WHERE id IN (:trackIds)")
    suspend fun getExistIds(trackIds: List<String>): List<String>


    @Query("SELECT * FROM favorites_track_table WHERE id = :trackId")
    suspend fun getExistTrack(trackId: String): TrackEntity?
}