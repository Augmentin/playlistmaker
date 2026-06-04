package com.example.playlistmaker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.data.HistoryRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.ItunesClient
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.preferences.PreferencesConstants

object Creator {
    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideHistoryInteractor(content: Context): HistoryInteractor{
        return HistoryInteractorImpl(getHistoryRepository(content))
    }

    private fun getHistoryRepository(content: Context): HistoryRepository{
        return HistoryRepositoryImpl(content.getSharedPreferences(
            PreferencesConstants.PLAYLISTMAKET_PREFERENCE,
            MODE_PRIVATE
        ))
    }
    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(ItunesClient())
    }

}