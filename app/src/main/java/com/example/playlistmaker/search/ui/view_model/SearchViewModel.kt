package com.example.playlistmaker.search.ui.view_model

import android.content.Context

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.playlistmaker.R
import com.example.playlistmaker.db.domain.api.FavoritesTracksInteractor

import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.TrackData
import com.example.playlistmaker.util.Debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(private val context: Context, private val tracksInteractor: TrackInteractor,
                      private val  favoritesTracksInteractor: FavoritesTracksInteractor ): ViewModel()  {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String = ""
    private val trackSearchDebounce =
        Debounce<String>(
            delayMillis = SEARCH_DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
            useLastParam = true
        ) { changedText ->
            searchRequest(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        trackSearchDebounce.cancel()
        if(latestSearchText.isNotEmpty()){
            trackSearchDebounce(changedText)
        }

    }

    fun updateFavorites() {
        val currentState = stateLiveData.value as? SearchState.Content ?: return
        val currentTracks = currentState.tracks

        if (currentTracks.isEmpty()) return
        viewModelScope.launch {
            val favoriteIds = favoritesTracksInteractor
                .getExistTracks(currentTracks.map { it.trackId })
                .first()
                .toHashSet()

            val updatedTracks = currentTracks.map { track ->
                track.copy(
                    isFavorite = track.trackId in favoriteIds
                )
            }
            renderState(
                SearchState.Content(updatedTracks)
            )
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
            val txt = newSearchText;
            viewModelScope.launch {
                tracksInteractor.searchTracks(newSearchText).collect {
                    pair ->
                    if(txt == latestSearchText){
                        processResult(pair.first, pair.second)
                    }
                }
            }
        }
    }


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}