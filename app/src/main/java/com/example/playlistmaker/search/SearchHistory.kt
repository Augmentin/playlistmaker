package com.example.playlistmaker.search

import android.content.SharedPreferences
import com.example.playlistmaker.network.itunes.TrackData
import com.example.playlistmaker.preferences.PreferencesConstants.PLAYLISTMAKET_TRACK_HISTORY_KEY
import com.google.gson.Gson
import androidx.core.content.edit

class SearchHistory(val sharedPreferences: SharedPreferences) {

    val historyArray: MutableList<TrackData> = loadHistory()



    private fun loadHistory(): MutableList<TrackData> {
        val json = sharedPreferences.getString(PLAYLISTMAKET_TRACK_HISTORY_KEY, null)
            ?: return mutableListOf()
        val list: Array<TrackData>? = Gson().fromJson(json, Array<TrackData>::class.java)
        val mutableList: MutableList<TrackData>? = list?.toMutableList()
        return mutableList ?: mutableListOf()
    }

    fun saveHistory(){
        sharedPreferences.edit { putString(PLAYLISTMAKET_TRACK_HISTORY_KEY, Gson().toJson(historyArray) ) }
    }

    fun clearHistory(){
        sharedPreferences.edit { remove(PLAYLISTMAKET_TRACK_HISTORY_KEY) }
        historyArray.clear()
    }

    fun addToHistory(track: TrackData ){
        historyArray.removeAll { it.trackId == track.trackId }
        historyArray.add(0, track)
        if (historyArray.size > 10) {
            historyArray.subList(10, historyArray.size).clear()
        }
        saveHistory()
    }



}