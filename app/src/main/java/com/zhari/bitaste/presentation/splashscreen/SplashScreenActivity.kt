package com.zhari.bitaste.presentation.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.zhari.bitaste.presentation.main.MainActivity
import com.zhari.bitaste.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.zhari.bitaste.data.repository.UserRepositoryImpl
import com.zhari.bitaste.presentation.login.LoginActivity
import com.zhari.bitaste.utils.GenericViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.zhari.bitaste.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): SplashViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return SplashViewModel(repo)
    }

    private val binding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkIfUserLogin()
    }

    private fun checkIfUserLogin() {
        lifecycleScope.launch {
            delay(2000)
            if (viewModel.isUserLoggedIn()) {
                navigateToMain()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })

    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}