package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel

import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.launch

class TrackToPlaylistModel(val trackData: TrackData, val playlistInteractor: PlaylistInteractor) : ViewModel()  {


    private val stateLiveData = MutableLiveData<TrackToPlaylistsState>()
    fun observeState(): LiveData<TrackToPlaylistsState> = stateLiveData


    fun addTrack(playlistModel: PlaylistModel){
        viewModelScope.launch {
            try {
                val playlistId =  playlistModel.id
                if(playlistId != null){
                    val alreadyAdded =  playlistInteractor.isTrackInPlaylist(
                        playlistId = playlistId ,
                        trackId = trackData.trackId,
                    )
                    if (alreadyAdded) {
                        renderState(TrackToPlaylistsState.AlreadyAdded(playlistModel.name))
                    } else {
                        playlistInteractor.addTrackToPlaylist(
                            playlistId = playlistId,
                            track = trackData,
                        )
                        renderState(TrackToPlaylistsState.AddedSuccess(playlistModel.name))
                    }
                }
            }catch (e: Exception){
                Log.e("TrackToPlaylist", e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun openMenu(){
        renderState(TrackToPlaylistsState.Opened)
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
            renderState(TrackToPlaylistsState.Empty())
        } else {
            renderState(TrackToPlaylistsState.Content(playlists))
        }
    }

    private fun renderState(state: TrackToPlaylistsState) {
        stateLiveData.postValue(state)
    }



}