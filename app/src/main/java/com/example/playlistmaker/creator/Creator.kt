package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.preferences.PreferencesConstants
import com.example.playlistmaker.search.data.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.ItunesClient
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl

object Creator {
    fun provideTracksInteractor(content: Context): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository(content))
    }

    fun provideHistoryInteractor(content: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(content))
    }

    private fun getHistoryRepository(content: Context): HistoryRepository {
        return HistoryRepositoryImpl(
            content.getSharedPreferences(
                PreferencesConstants.PLAYLISTMAKET_PREFERENCE,
                Context.MODE_PRIVATE
            )
        )
    }
    private fun getTracksRepository(content: Context): TrackRepository {
        return TrackRepositoryImpl(ItunesClient(content))
    }

}