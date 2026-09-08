package com.example.playlistmaker.medialibrary.domain.api

import android.net.Uri

interface SaveFileRepository {

    fun saveImageToInternalStorage(uri: Uri): String
}