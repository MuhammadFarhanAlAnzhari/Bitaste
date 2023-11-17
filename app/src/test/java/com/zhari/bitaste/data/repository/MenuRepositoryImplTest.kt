package com.zhari.bitaste.data.repository

import app.cash.turbine.test
import com.zhari.bitaste.data.network.api.datasource.BitasteDataSource
import com.zhari.bitaste.data.network.api.model.category.CategoriesResponse
import com.zhari.bitaste.data.network.api.model.category.CategoryItemResponse
import com.zhari.bitaste.data.network.api.model.menu.MenuItemResponse
import com.zhari.bitaste.data.network.api.model.menu.MenuResponse
import com.zhari.bitaste.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {

    @MockK
    lateinit var remoteDataSource: BitasteDataSource
    private lateinit var repository: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = MenuRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get categories, with result loading`() {
        val mockCategoriesResponse = mockk<CategoriesResponse>()
        runTest {
            coEvery { remoteDataSource.getCategories() } returns mockCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify {
                    remoteDataSource.getCategories()
                }
            }
        }
    }

    @Test
    fun `get categories, with result success`() {
        val fakeCategoryResponse = CategoryItemResponse(
            id = 124,
            imageUrl = "url",
            name = "name",
            slug = "slug"
        )
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "success",
            data = listOf(fakeCategoryResponse)
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 124)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result empty`() {
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result error`() {
        runTest {
            coEvery { remoteDataSource.getCategories() } throws IllegalAccessException("Mock error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get menus, with result loading`() {
        val fakeMenuResponse = mockk<MenuResponse>()
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns fakeMenuResponse
            repository.getMenus("makanan").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result success`() {
        val fakeMenuItemResponse = MenuItemResponse(
            id = 124,
            restaurantAddress = "bandung",
            detail = "description",
            price = 20000.0,
            formattedPrice = "20000",
            imageUrl = "urlImage",
            name = "name"
        )
        val fakeMenusResponse = MenuResponse(
            code = 200,
            status = true,
            message = "success",
            data = listOf(fakeMenuItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns fakeMenusResponse
            repository.getMenus("makanan").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 124)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result empty`() {
        val fakeMenusResponse = MenuResponse(
            code = 200,
            status = true,
            message = "success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns fakeMenusResponse
            repository.getMenus().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result error`() {
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } throws IllegalAccessException("Mock error")
            repository.getMenus("makanan").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get menuList, with result loading`() {
        val fakeMenuResponse = mockk<MenuResponse>()
        runTest {
            coEvery { remoteDataSource.getMenusList() } returns fakeMenuResponse
            repository.getMenuList().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getMenusList() }
            }
        }
    }

    @Test
    fun `get menuList, with result success`() {
        val fakeMenuItemResponse = MenuItemResponse(
            id = 124,
            restaurantAddress = "bandung",
            detail = "description",
            price = 20000.0,
            formattedPrice = "20000",
            imageUrl = "urlImage",
            name = "name"
        )
        val fakeMenusResponse = MenuResponse(
            code = 200,
            status = true,
            message = "success",
            data = listOf(fakeMenuItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getMenusList() } returns fakeMenusResponse
            repository.getMenuList().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 124)
                coVerify { remoteDataSource.getMenusList() }
            }
        }
    }

    @Test
    fun `get menuList, with result empty`() {
        val fakeMenusResponse = MenuResponse(
            code = 200,
            status = true,
            message = "success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getMenusList() } returns fakeMenusResponse
            repository.getMenuList().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getMenusList() }
            }
        }
    }

    @Test
    fun `get menuList, with result error`() {
        runTest {
            coEvery { remoteDataSource.getMenusList() } throws IllegalAccessException("Mock error")
            repository.getMenuList().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getMenusList() }
            }
        }
    }
}
