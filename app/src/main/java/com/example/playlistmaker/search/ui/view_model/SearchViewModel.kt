package com.example.playlistmaker.search.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.playlistmaker.R

import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val context: Context, private val tracksInteractor: TrackInteractor ): ViewModel()  {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L


    }
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String = ""
    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchRequest(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText

        if(latestSearchText.isNotEmpty()){
            trackSearchDebounce(changedText)
        }

    }

    fun searchNow(changedText: String){
        this.latestSearchText = changedText
        searchRequest( changedText)
    }

    fun clearSearch(){
        renderState(
            SearchState.Empty(
                message = "",
                img = -1
            )
        )
    }

    private fun processResult(trackList: List<TrackData>?, errorCode: Int?){
        val tracks = mutableListOf<TrackData>()
        if (trackList != null) {
            tracks.addAll(trackList)
        }

        when {
            errorCode != null -> {
                if(errorCode == -1){
                    renderState(
                        SearchState.Error(
                            errorMessage = context.getString(R.string.connect_problem_title),
                            errorPlaceholderMessage = context.getString(R.string.connect_problem_message),
                            img = R.drawable.connection_fail
                        )
                    )
                }else{
                    renderState(
                        SearchState.Error(
                            errorMessage = context.getString(R.string.error),
                            img = R.drawable.not_found
                        )
                    )
                }


            }
            tracks.isEmpty() -> {
                renderState(
                    SearchState.Empty(
                        message = context.getString(R.string.not_found),
                        img = R.drawable.not_found
                    )
                )
            }
            else -> {
                renderState(
                    SearchState.Content(
                        tracks = tracks,
                    )
                )
            }
        }
    }
     private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                SearchState.Loading
            )
            viewModelScope.launch {
                tracksInteractor.searchTracks(newSearchText).collect {
                    pair -> processResult(pair.first, pair.second)
                }
            }
        }
    }


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}