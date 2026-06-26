package com.example.playlistmaker.search.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator

import com.example.playlistmaker.R
import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.TrackData

class SearchViewModel(private val context: Context): ViewModel()  {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as App)
                SearchViewModel(app)
            }
        }
    }
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    private var tracksInteractor: TrackInteractor = Creator.provideTracksInteractor(context)
    private var latestSearchText: String = ""
    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        if(latestSearchText.isNotEmpty()){
            val searchRunnable = Runnable { searchRequest(changedText) }
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        }

    }

    fun searchNow(changedText: String){
        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        searchRequest( changedText)
    }
     private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                SearchState.Loading
            )

            tracksInteractor.searchTracks(newSearchText, object : TrackInteractor.TracksConsumer {
                override fun consume(
                    trackList: List<TrackData>?,
                    errorCode: Int?
                ) {
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

            })
        }
    }


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}