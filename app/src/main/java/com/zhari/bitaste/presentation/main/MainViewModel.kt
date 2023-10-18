package com.zhari.bitaste.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zhari.bitaste.data.local.datastore.UserPreferenceDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val userPreferenceDataSource: UserPreferenceDataSource) : ViewModel() {
    val userGridModeLiveData = userPreferenceDataSource.getUserGridModePrefFlow().asLiveData(Dispatchers.IO)

    fun setGridModePref(isUsingGridMode : Boolean){
        viewModelScope.launch {
            userPreferenceDataSource.setUserGridModePref(isUsingGridMode)
        }
    }
}