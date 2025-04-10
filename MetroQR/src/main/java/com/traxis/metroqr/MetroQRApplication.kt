package com.traxis.metroqr

import android.app.Application
import com.traxis.metroqr.utils.SettingsManager

class MetroQRApplication : Application() {
    private lateinit var settingsManager: SettingsManager

    override fun onCreate() {
        super.onCreate()
        settingsManager = SettingsManager(this)
        settingsManager.initialize()
    }

    fun getSettingsManager(): SettingsManager = settingsManager
} 