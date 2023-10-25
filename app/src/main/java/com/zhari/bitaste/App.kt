package com.zhari.bitaste

import android.app.Application
import com.zhari.bitaste.data.local.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}
