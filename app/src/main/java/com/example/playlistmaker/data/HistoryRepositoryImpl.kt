package com.example.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.data.dto.HistoryTrackTdo

import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.TrackData
import com.example.playlistmaker.preferences.PreferencesConstants.PLAYLISTMAKET_TRACK_HISTORY_KEY
import com.google.gson.Gson

class HistoryRepositoryImpl(private val sharedPreferences : SharedPreferences) : HistoryRepository {
    val historySaveKey = PLAYLISTMAKET_TRACK_HISTORY_KEY
    override fun get(): MutableList<TrackData> {
        val json = sharedPreferences.getString(historySaveKey, null)
            ?: return mutableListOf()
        val list: Array<HistoryTrackTdo>? = Gson().fromJson(json, Array<HistoryTrackTdo>::class.java)
        var mutableList: MutableList<HistoryTrackTdo>? = list?.toMutableList()
        mutableList = mutableList ?: mutableListOf()
        return mutableList.map { it.toTrackData() } as MutableList<TrackData>
    }

    override fun save(list: MutableList<TrackData>) {
        val array = list.map {
                it.toHistoryTrackTdo()
        }
        sharedPreferences.edit { putString(historySaveKey, Gson().toJson(array) ) }

    }

    override fun clear() {
        sharedPreferences.edit { remove(historySaveKey) }
    }
}