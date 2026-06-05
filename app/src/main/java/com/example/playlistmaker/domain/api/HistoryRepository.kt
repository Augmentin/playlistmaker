package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface HistoryRepository {

    fun get():MutableList<TrackData>

    fun save(list: MutableList<TrackData>)

    fun clear();
}