package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TrackData

interface HistoryRepository {

    fun get():MutableList<TrackData>

    fun save(list: MutableList<TrackData>)

    fun clear();
}