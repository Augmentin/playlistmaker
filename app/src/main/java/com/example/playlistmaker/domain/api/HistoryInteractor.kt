package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface HistoryInteractor {

    fun add(track: TrackData )

    fun clear()

    fun get():MutableList<TrackData>

    fun size(): Int
}