package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.preferences.PreferencesConstants

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val interactor = Creator.provideSettingsInteractor(this)
        interactor.updateThemeSetting(interactor.getThemeSettings())

    }
}