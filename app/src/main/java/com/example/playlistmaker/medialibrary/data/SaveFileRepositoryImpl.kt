package com.example.playlistmaker.medialibrary.data

import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.api.SaveFileRepository
import android.content.Context
import java.util.UUID
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SaveFileRepositoryImpl(  private val context: Context ) : SaveFileRepository {


    override fun saveImageToInternalStorage(uri: Uri): String {
        // Получаем приватную папку приложения для изображений
        val picturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: throw IOException("Не удалось получить папку Pictures")

        // Создаём папку для обложек плейлистов
        val filePath = File(picturesDirectory, "playlist_covers")

        if (!filePath.exists() && !filePath.mkdirs()) {
            throw IOException("Не удалось создать папку для обложек")
        }


        val file = File(filePath, "playlist_cover_${UUID.randomUUID()}.jpg")

        val bitmap = context.contentResolver
            .openInputStream(uri)
            ?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
            ?: throw IOException("Не удалось открыть изображение")

        // Сохраняем изображение в JPEG
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
        bitmap.recycle()


        return file.absolutePath
    }
}