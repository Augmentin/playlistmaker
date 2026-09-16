package com.example.playlistmaker.medialibrary.domain.impl


import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.api.SaveFileRepository

class SaveFileInteractorImpl(val saveFileRepositoryImpl: SaveFileRepository): SaveFileInteractor {

    override suspend  fun saveToInternalStorage(uri: Uri): String {
        return saveFileRepositoryImpl.saveToInternalStorage(uri)
    }

    override fun getFromInternalStorage(fileName: String): Uri? {
        return saveFileRepositoryImpl.getFromInternalStorage(fileName)
    }

    override suspend fun deleteInternalStorage(fileName: String){
        return saveFileRepositoryImpl.deleteInternalStorage(fileName)
    }
}