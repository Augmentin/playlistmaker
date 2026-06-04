package com.example.playlistmaker.domain.impl

import androidx.core.content.edit
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.TrackData
import com.example.playlistmaker.preferences.PreferencesConstants.PLAYLISTMAKET_TRACK_HISTORY_KEY
import com.google.gson.Gson

class HistoryInteractorImpl(private val  rep : HistoryRepository) : HistoryInteractor{
    private val historyArray: MutableList<TrackData> = loadHistory()


    override fun add(track: TrackData) {
        historyArray.removeAll { it.trackId == track.trackId }
        historyArray.add(0, track)
        if (historyArray.size > 10) {
            historyArray.subList(10, historyArray.size).clear()
        }
        saveHistory()
    }

    override fun clear() {
        rep.clear()
        historyArray.clear()
    }

    override fun get(): MutableList<TrackData> {
        return historyArray
    }

    override fun size(): Int {
       return historyArray.size
    }

    private fun loadHistory() : MutableList<TrackData> {
        return rep.get()
    }
    private fun saveHistory(){
        rep.save(historyArray);
    }
}