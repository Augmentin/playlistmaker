package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.playlistmaker.db.data.entity.TrackEntity

@Dao
interface TrackDao {


    @Upsert
    suspend fun upsertFavoriteTrack(track: TrackEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackIfAbsent(track: TrackEntity)
    @Query(
        """
    UPDATE tracks
    SET favorite = :favorite
    WHERE id = :trackId
    """
    )
    suspend fun updateFavorite(
        trackId: String,
        favorite: Boolean,
    )

    @Transaction
    suspend fun removeTrackFromFavorites(trackId: String) {
        updateFavorite(
            trackId = trackId,
            favorite = false,
        )
        deleteTrackIfUnused(trackId)
    }

    @Query(
        """
        DELETE FROM tracks
        WHERE id = :trackId
          AND favorite = 0
          AND NOT EXISTS (
              SELECT 1 FROM playlist_tracks
              WHERE playlist_tracks.trackId = tracks.id
          )
        """
    )
    suspend fun deleteTrackIfUnused(trackId: String)

    @Query("SELECT * FROM tracks WHERE favorite=1 ORDER BY created DESC")
    suspend fun getFavoriteTracks(): List<TrackEntity>


    @Query("SELECT * FROM tracks WHERE id = :trackId  AND favorite=1")
    suspend fun getExistFavoriteTrack(trackId: String): TrackEntity?
}