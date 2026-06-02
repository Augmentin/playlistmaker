package com.example.playlistmaker

import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.ItunesClient
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(ItunesClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}