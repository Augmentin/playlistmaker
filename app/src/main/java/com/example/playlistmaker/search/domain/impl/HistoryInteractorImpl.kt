package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.models.TrackData

class HistoryInteractorImpl(private val  rep : HistoryRepository) : HistoryInteractor {


    override fun add(track: TrackData) {
        val array = rep.get()
        array.removeAll { it.trackId == track.trackId }
        array.add(0, track)
        if (array.size > 10) {
            array.subList(10, array.size).clear()
        }
        saveHistory(array)
    }

    override fun clear() {
        rep.clear()
    }

    override fun get(): MutableList<TrackData> {
        return rep.get()
    }

    override fun size(): Int {
       return rep.get().size
    }


    private fun saveHistory(array: MutableList<TrackData>){
        rep.save(array);
    }
}