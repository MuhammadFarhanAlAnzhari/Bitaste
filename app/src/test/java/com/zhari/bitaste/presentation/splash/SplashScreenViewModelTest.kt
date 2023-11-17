package com.zhari.bitaste.presentation.splash

import com.zhari.bitaste.data.repository.UserRepository
import com.zhari.bitaste.presentation.splashscreen.SplashViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class SplashScreenViewModelTest {

    @MockK
    private lateinit var repo: UserRepository

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SplashViewModel(repo)
    }

    @Test
    fun `user logged in`() {
        every { repo.isLoggedIn() } returns true
        val result = viewModel.isUserLoggedIn()
        verify { repo.isLoggedIn() }
        assertTrue(result)
    }
}
