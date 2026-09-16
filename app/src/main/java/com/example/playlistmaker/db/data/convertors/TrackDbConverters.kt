package com.example.playlistmaker.db.data.convertors

import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.data.entity.PlaylistWithTrackCount
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
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

    fun map(playlistModel: PlaylistModel): PlaylistEntity{
        return playlistModel.toPlaylistEntity()
    }

    fun map(playlistModel: PlaylistEntity): PlaylistModel{
        return playlistModel.toPlaylistModel()
    }

    fun map(playlistModel: PlaylistWithTrackCount): PlaylistModel{
        return playlistModel.toPlaylistModel()
    }
}