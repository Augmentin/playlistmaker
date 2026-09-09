package com.example.playlistmaker.medialibrary.domain.api

import android.net.Uri

interface SaveFileRepository {

    suspend fun saveToInternalStorage(uri: Uri): String

    fun getFromInternalStorage(fileName: String):Uri?

    suspend fun deleteInternalStorage(fileName: String)
}