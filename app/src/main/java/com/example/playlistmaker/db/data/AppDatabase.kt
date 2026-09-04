package com.example.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.db.data.dao.PlaylistDao
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.data.entity.PlaylistTracks
import com.example.playlistmaker.db.data.entity.TrackEntity

@Database(
    version = 2,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTracks::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            ALTER TABLE favorites_track_table
            RENAME TO tracks
            """.trimIndent()
                )

                database.execSQL(
                    """
    ALTER TABLE tracks
    ADD COLUMN favorite INTEGER NOT NULL DEFAULT 0
    """.trimIndent()
                )
                database.execSQL(
                    """
    UPDATE tracks
    SET favorite = 1
    """.trimIndent()
                )
                database.execSQL(
                    """
            CREATE INDEX IF NOT EXISTS index_tracks_favorite
            ON tracks(favorite)
            """.trimIndent()
                )
                database.execSQL(
                    """
    CREATE TABLE IF NOT EXISTS playlist_table (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        name TEXT NOT NULL,
        imagePath TEXT,
        description TEXT
    )
    """.trimIndent()
                )
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS playlist_tracks (
                playlistId INTEGER NOT NULL,
                trackId TEXT NOT NULL,
                addedAt INTEGER NOT NULL,
                PRIMARY KEY(playlistId, trackId),
                FOREIGN KEY(playlistId)
                    REFERENCES playlist_table(id)
                    ON DELETE CASCADE,
                FOREIGN KEY(trackId)
                    REFERENCES tracks(id)
                    ON DELETE CASCADE
            )
            """.trimIndent()
                )

                database.execSQL(
                    """
            CREATE INDEX IF NOT EXISTS index_playlist_tracks_playlistId
            ON playlist_tracks(playlistId)
            """.trimIndent()
                )

                database.execSQL(
                    """
            CREATE INDEX IF NOT EXISTS index_playlist_tracks_trackId
            ON playlist_tracks(trackId)
            """.trimIndent()
                )
            }
        }
    }

}

