package com.example.playlistmaker.playlist.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.sharing.domain.impl.SharingInteractor

import kotlinx.coroutines.launch


class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel()  {

    private val playlistLiveData = MutableLiveData<PlaylistModel>()
    fun observePlaylistLiveData(): LiveData<PlaylistModel> = playlistLiveData

    private val tracksLiveData = MutableLiveData<List<TrackData>>()
    fun observeTracksState(): LiveData<List<TrackData>> = tracksLiveData

    private val state = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = state


    init {
        observePlaylist()
        observeTracks()
        state.postValue(PlaylistState.OpenTracksList)
    }
    fun share(text: String){
        sharingInteractor.shareApp(text)
    }

    fun getPlaylist(): PlaylistModel?{
        return playlistLiveData.value
    }
    fun getTracks(): List<TrackData> {
        return tracksLiveData.value ?: listOf()
    }
    fun openMenu(){
        state.postValue(PlaylistState.OpenMenu)
    }
    fun hideMenu(){
        state.postValue(PlaylistState.OpenTracksList)
    }
    fun deleteTrack(track: TrackData) {
        viewModelScope.launch {
            try {
                playlistInteractor.deleteTrackFromPlaylist(
                    playlistId = playlistId,
                    trackId = track.trackId,
                )
            }catch (exception: Exception) {
                Log.e("PlaylistViewModel", "Не удалось удалить трек", exception)
            }
        }
    }

    fun deletePlaylist(playlistId: Long){
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }

    private fun observeTracks(){
        viewModelScope.launch {
            playlistInteractor
                .getTracks(playlistId)
                .collect {
                    tracks -> renderTracks(tracks)
                }
        }
    }

    private fun observePlaylist() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistById(playlistId)
                .collect { playlist ->  renderPlaylist(playlist) }
        }
    }

    private fun renderTracks(tracks: List<TrackData>){
        tracksLiveData.postValue(tracks)
    }
    private fun renderPlaylist(playlist: PlaylistModel?) {
        if(playlist != null){
            playlistLiveData.postValue(playlist!!)
        }
    }
}