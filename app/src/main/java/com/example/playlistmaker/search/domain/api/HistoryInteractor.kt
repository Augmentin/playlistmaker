package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TrackData

interface HistoryInteractor {

    fun add(track: TrackData )

    fun clear()

    fun get():MutableList<TrackData>

    fun size(): Int


}