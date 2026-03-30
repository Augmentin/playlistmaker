package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
const val PLAYLISTMAKET_THEME_KEY = "plm_theme"
const val PLAYLISTMAKET_PREFERENCE = "plm_preferences"
class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(PLAYLISTMAKET_THEME_KEY, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPrefs = getSharedPreferences(PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE)
        if(darkThemeEnabled != sharedPrefs.getBoolean(PLAYLISTMAKET_THEME_KEY, false)){
            sharedPrefs.edit()
                .putBoolean(PLAYLISTMAKET_THEME_KEY, darkThemeEnabled)
                .apply()
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