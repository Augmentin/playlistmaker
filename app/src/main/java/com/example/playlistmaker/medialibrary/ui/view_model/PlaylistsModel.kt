package com.example.playlistmaker.medialibrary.ui.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.launch


class PlaylistsModel(val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    init {

    }

    fun update() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { processResult(it) }
        }
    }

    private fun processResult(playlists: List<PlaylistModel>) {
        Log.d("data_", playlists.toString())
        if (playlists.isEmpty()) {
            renderState(PlaylistsState.Empty())
        } else {
            renderState(PlaylistsState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }
}