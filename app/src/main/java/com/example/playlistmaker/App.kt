package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.preferences.PreferencesConstants.PLAYLISTMAKET_PREFERENCE
import com.example.playlistmaker.preferences.PreferencesConstants.PLAYLISTMAKET_THEME_KEY


class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(PLAYLISTMAKET_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        if(darkTheme != darkThemeEnabled){
            val sharedPrefs = getSharedPreferences(PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(PLAYLISTMAKET_THEME_KEY, darkThemeEnabled)
                .apply()
            darkTheme = darkThemeEnabled;
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}