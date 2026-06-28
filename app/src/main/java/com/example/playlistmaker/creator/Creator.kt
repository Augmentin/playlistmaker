package com.example.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.preferences.PreferencesConstants
import com.example.playlistmaker.search.data.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.ItunesApiService
import com.example.playlistmaker.search.data.network.ItunesClient
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.impl.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import kotlin.lazy

object Creator {
    fun provideTracksInteractor(content: Context): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository(content))
    }

    fun provideHistoryInteractor(content: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(content))
    }

    fun provideSettingsInteractor(content: Context): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository(content))
    }
    fun provideSharingInteractor(content: Context): SharingInteractor{
        return SharingInteractorImpl(
            getSharingRepository(content),
        )
    }

    fun getMediaPlayer(): MediaPlayer{
        return MediaPlayer()
    }
    private fun getSharingRepository(content: Context): ExternalNavigator{
        return ExternalNavigator(content)
    }
    private fun getSettingsRepository(content: Context): SettingsRepository{
        return SettingsRepositoryImpl(content)
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