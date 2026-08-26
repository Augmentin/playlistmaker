package com.example.playlistmaker.medialibrary.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.db.domain.api.FavoritesTracksInteractor
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.launch

class FavouritesModel(private val favoritesTracksInteractor: FavoritesTracksInteractor): ViewModel() {


    private val stateLiveData = MutableLiveData<FavouritesTracksState>()
    fun observeState(): LiveData<FavouritesTracksState> = stateLiveData


    fun update(){
        viewModelScope.launch {
            favoritesTracksInteractor.favoritesTracks().collect {
                processResult(it)
            }
        }
    }
    private fun processResult(tracks: List<TrackData>) {
        if (tracks.isEmpty()) {
            renderState(FavouritesTracksState.Empty())
        } else {
            renderState(FavouritesTracksState.Content(tracks))
        }
    }
    private fun renderState(state: FavouritesTracksState) {
        stateLiveData.postValue(state)
    }
}