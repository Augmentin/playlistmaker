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

    fun provideSettingsInteractor(content: Context): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository(content))
    }
    fun provideSharingInteractor(content: Context): SharingInteractor{
        return SharingInteractorImpl(
            getSharingRepository(content),
        )
    }

    private fun getSharingRepository(content: Context): ExternalNavigator{
        return ExternalNavigator(content)
    }
    private fun getSettingsRepository(content: Context): SettingsRepository{
        return SettingsRepositoryImpl(content)
    }

}