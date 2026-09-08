package com.example.playlistmaker.medialibrary.domain.impl


import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.api.SaveFileRepository

class SaveFileInteractorImpl(val saveFileRepositoryImpl: SaveFileRepository): SaveFileInteractor {

    override fun saveImageToInternalStorage(uri: Uri): String {
        return saveFileRepositoryImpl.saveImageToInternalStorage(uri)
    }
}