package com.zhari.bitaste.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.zhari.bitaste.data.repository.UserRepository

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class SplashViewModel(private val repository: UserRepository) : ViewModel() {

    fun isUserLoggedIn() = repository.isLoggedIn()

}