package com.example.playlistmaker.medialibrary.domain.api

import android.net.Uri

interface SaveFileInteractor {
    fun saveImageToInternalStorage(uri: Uri): String
}