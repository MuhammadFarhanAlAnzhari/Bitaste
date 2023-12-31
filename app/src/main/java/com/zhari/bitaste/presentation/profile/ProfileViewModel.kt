package com.zhari.bitaste.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhari.bitaste.data.repository.UserRepository
import com.zhari.bitaste.utils.ResultWrapper
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: UserRepository) : ViewModel() {

    private val _updateProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val updateProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _updateProfileResult

    fun getCurrentUser() = repo.getCurrentUser()
    fun createChangePasswordReq() {
        repo.sendChangePasswordRequestByEmail()
    }
    fun doLogout() {
        repo.doLogout()
    }
    fun updateProfile(fullName: String) {
        viewModelScope.launch {
            repo.updateProfile(fullName).collect {
                _updateProfileResult.postValue(it)
            }
        }
    }
}
