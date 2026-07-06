package com.example.playlistmaker.medialibrary.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R


class PlayListModel(private val context: Context) : ViewModel() {
    private val stateLiveData = MutableLiveData<PlayListState>()
    fun observeState(): LiveData<PlayListState> = stateLiveData

    init {
        stateLiveData.postValue(PlayListState.Empty(context.getString(R.string.empty_playlist), R.drawable.not_found))
    }
}