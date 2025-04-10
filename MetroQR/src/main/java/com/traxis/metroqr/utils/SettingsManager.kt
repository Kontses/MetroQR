package com.traxis.metroqr.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class SettingsManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "MetroQRPrefs"
        const val KEY_LANGUAGE = "language"
        const val KEY_THEME = "theme"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
    }

    fun setLanguage(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
        updateLanguage()
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, "el") ?: "el"
    }

    fun setTheme(theme: String) {
        prefs.edit().putString(KEY_THEME, theme).apply()
        updateTheme()
    }

    fun getTheme(): String {
        return prefs.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
    }

    private fun updateLanguage() {
        val locale = Locale(getLanguage())
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun updateTheme() {
        when (getTheme()) {
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    fun initialize() {
        updateLanguage()
        updateTheme()
    }
} 