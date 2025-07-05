package com.railmind

import android.app.Application
import com.railmind.data.AppDatabase

class RailMindApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
