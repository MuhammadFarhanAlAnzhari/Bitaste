
package com.zhari.bitaste.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zhari.bitaste.data.repository.MenuRepository
import com.zhari.bitaste.data.repository.UserRepository
import com.zhari.bitaste.model.User
import com.zhari.bitaste.tools.MainCoroutineRule
import com.zhari.bitaste.tools.getOrAwaitValue
import com.zhari.bitaste.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule.MainCoroutineRule(UnconfinedTestDispatcher())

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var menuRepo: MenuRepository

    @MockK
    lateinit var userRepo: UserRepository

    @MockK
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { menuRepo.getMenus(any()) } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }

        coEvery { menuRepo.getCategories() } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }

        coEvery { userRepo.getCurrentUser() } returns User("fullName", "url", "email")

        viewModel = spyk(
            HomeViewModel(
                menuRepo = menuRepo,
                userRepo = userRepo
            )
        )
    }

    @Test
    fun getCategories() {
        val result = viewModel.categories.getOrAwaitValue()
        assertEquals(result.payload?.size, 4)
        coVerify { menuRepo.getCategories() }
    }

    @Test
    fun getMenus() {
        viewModel.getMenus()
        val result = viewModel.menuList.getOrAwaitValue()
        assertEquals(result.payload?.size, 3)
        coVerify { menuRepo.getMenus() }
    }

    @Test
    fun getCurrentUser() {
        val user = User("name", "url", "email@gmail.com")
        every { userRepo.getCurrentUser() } returns user
        val result = viewModel.getCurrentUser()
        assertTrue(result == user)
    }
}
