package com.example.playlistmaker.medialibrary.data

import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.api.SaveFileRepository
import android.content.Context
import java.util.UUID
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SaveFileRepositoryImpl(  private val context: Context ) : SaveFileRepository {


    override fun saveToInternalStorage(uri: Uri): String {
        val picturesDirectory = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        ) ?: throw IOException("Не удалось получить папку Pictures")

        val directory = File(
            picturesDirectory,
            PLAYLIST_COVERS_DIRECTORY
        )

        if (!directory.exists() && !directory.mkdirs()) {
            throw IOException("Не удалось создать папку для обложек")
        }

        val fileName = "playlist_cover_${UUID.randomUUID()}.jpg"
        val file = File(directory, fileName)

        val bitmap = context.contentResolver
            .openInputStream(uri)
            ?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
            ?: throw IOException("Не удалось открыть изображение")

        try {
            FileOutputStream(file).use { outputStream ->
                val wasSaved = bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    30,
                    outputStream
                )

                if (!wasSaved) {
                    throw IOException("Не удалось сохранить изображение")
                }
            }
        } finally {
            bitmap.recycle()
        }

        return fileName
    }

    override fun getFromInternalStorage(fileName: String): Uri? {
        if (fileName.isBlank()) {
            return null
        }

        val picturesDirectory = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        ) ?: return null

        val directory = File(
            picturesDirectory,
            PLAYLIST_COVERS_DIRECTORY
        )

        val file = File(directory, fileName)

        return if (file.exists()) {
            file.toUri()
        } else {
            null
        }
    }

    override fun deleteInternalStorage(fileName: String) {
        if (fileName.isNotBlank()) {
            val picturesDirectory = context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            )
            val directory = File(
                picturesDirectory,
                PLAYLIST_COVERS_DIRECTORY
            )
            val file = File(directory, fileName)
            file.delete()
        }
    }
    companion object{
        private const val PLAYLIST_COVERS_DIRECTORY = "playlist_covers"
    }
}