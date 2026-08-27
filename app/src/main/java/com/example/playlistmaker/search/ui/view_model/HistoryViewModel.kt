package com.example.playlistmaker.search.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.FavoritesTracksInteractor

import com.example.playlistmaker.search.domain.api.HistoryInteractor

import com.example.playlistmaker.search.domain.models.TrackData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyInteractor: HistoryInteractor) : ViewModel()  {

    private val stateLiveData = MutableLiveData<MutableList<TrackData>>()
    fun observeState(): LiveData<MutableList<TrackData>> = stateLiveData

    init {
        stateLiveData.postValue(historyInteractor.get())
    }

    fun add(track: TrackData){
        historyInteractor.add(track)
        val list = stateLiveData.value?.toMutableList() ?: mutableListOf()
        list.add(track)
        stateLiveData.postValue(list)
    }

    fun clear(){
        historyInteractor.clear()
        stateLiveData.postValue(mutableListOf())
    }

    fun size(): Int {
        return stateLiveData.value?.size ?: 0
    }
}