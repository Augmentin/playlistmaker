package com.example.playlistmaker.db.data.convertors

import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.search.data.toTrackData
import com.example.playlistmaker.search.data.toTrackDataEntity
import com.example.playlistmaker.search.domain.models.TrackData

class TrackDbConvertors {
    fun map(track: TrackData): TrackEntity {
        return track.toTrackDataEntity()
    }

    fun map(track: TrackEntity): TrackData {
        return track.toTrackData()
    }
}