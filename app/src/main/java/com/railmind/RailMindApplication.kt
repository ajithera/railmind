package com.railmind

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class RailMindApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize WorkManager
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
            
        WorkManager.initialize(this, config)
    }
}