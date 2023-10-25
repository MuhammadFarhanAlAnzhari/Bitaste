package com.zhari.bitaste.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.appDataStore by preferencesDataStore(
    //TODO : Change with your own preference app name
    name = "Bitaste"
)