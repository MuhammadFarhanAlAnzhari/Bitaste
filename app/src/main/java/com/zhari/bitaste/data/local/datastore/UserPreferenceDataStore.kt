package com.zhari.bitaste.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.zhari.bitaste.utils.PreferenceDataStoreHelper
import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataSource {
    suspend fun getUserGridModePref(): Boolean
    fun getUserGridModePrefFlow(): Flow<Boolean>
    suspend fun setUserGridModePref(isUsingGridMode: Boolean)
}

class UserPreferenceDataSourceImpl(private val preferenceHelper: PreferenceDataStoreHelper) :
    UserPreferenceDataSource {

    override suspend fun getUserGridModePref(): Boolean {
        return preferenceHelper.getFirstPreference(PREF_USER_GRID_MODE, false)
    }

    override fun getUserGridModePrefFlow(): Flow<Boolean> {
        return preferenceHelper.getPreference(PREF_USER_GRID_MODE, false)
    }

    override suspend fun setUserGridModePref(isUsingGridMode: Boolean) {
        return preferenceHelper.putPreference(PREF_USER_GRID_MODE, isUsingGridMode)
    }

    companion object {
        val PREF_USER_GRID_MODE = booleanPreferencesKey("PREF_USER_GRID_MODE")
    }
}
