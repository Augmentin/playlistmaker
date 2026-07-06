package com.example.playlistmaker.medialibrary.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R

class FavouritesModel(private val context: Context): ViewModel() {


    private val stateLiveData = MutableLiveData<FavouritesTracksState>()
    fun observeState(): LiveData<FavouritesTracksState> = stateLiveData

    init {
        stateLiveData.postValue(FavouritesTracksState.Empty(context.getString(R.string.empty_favorites), R.drawable.not_found))
    }
}