package com.example.playlistmaker.search.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.api.HistoryInteractor

import com.example.playlistmaker.search.domain.models.TrackData

class HistoryViewModel(private val context: Context ) : ViewModel()  {

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as App)
                HistoryViewModel(app)
            }
        }
    }
    private val stateLiveData = MutableLiveData<MutableList<TrackData>>()
    fun observeState(): LiveData<MutableList<TrackData>> = stateLiveData


    private var historyInteractor: HistoryInteractor;
    init {
        historyInteractor = Creator.provideHistoryInteractor(context)
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