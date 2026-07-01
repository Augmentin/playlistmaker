package com.example.playlistmaker

import android.app.Application

import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.dataModule
import com.example.playlistmaker.search.di.interactorModule
import com.example.playlistmaker.search.di.networkModule
import com.example.playlistmaker.search.di.repositoryModule
import com.example.playlistmaker.search.di.viewModelModule
import com.example.playlistmaker.settings.di.settingsModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.di.sharingModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule,
                networkModule,
                playerViewModelModule,
                settingsModule,
                sharingModule
            )
        }

        val interactor: SettingsInteractor = getKoin().get()
        interactor.updateThemeSetting(interactor.getThemeSettings())

    }
}