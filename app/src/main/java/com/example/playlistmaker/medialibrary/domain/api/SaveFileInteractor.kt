package com.example.playlistmaker.medialibrary.domain.api

import android.net.Uri

interface SaveFileInteractor {
    fun saveToInternalStorage(uri: Uri): String

    fun getFromInternalStorage(fileName: String):Uri?

    fun deleteInternalStorage(fileName: String)
}