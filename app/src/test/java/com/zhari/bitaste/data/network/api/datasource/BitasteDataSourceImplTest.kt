package com.zhari.bitaste.data.network.api.datasource

import com.zhari.bitaste.data.network.api.model.category.CategoriesResponse
import com.zhari.bitaste.data.network.api.model.menu.MenuResponse
import com.zhari.bitaste.data.network.api.model.order.OrderRequest
import com.zhari.bitaste.data.network.api.model.order.OrderResponse
import com.zhari.bitaste.data.network.api.service.RestaurantService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BitasteDataSourceImplTest {

    @MockK
    lateinit var service: RestaurantService
    private lateinit var dataSource: BitasteDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = BitasteDataSourceImpl(service)
    }

    @Test
    fun getMenus() {
        runTest {
            val mockResponse = mockk<MenuResponse>(relaxed = true)
            // memanggil data
            coEvery { service.getMenus(any()) } returns mockResponse
            val response = dataSource.getProducts("food")
            // memverify data apakah data sudah terpanggil
            coVerify { service.getMenus(any()) }
            // mencocok kan data
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategory() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            // memanggil data
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            // memverify data apakah data sudah terpanggil
            coVerify { service.getCategories() }
            // mencocok kan data
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getMenuList() {
        runTest {
            val mockResponse = mockk<MenuResponse>(relaxed = true)
            // memanggil data
            coEvery { service.getMenusList() } returns mockResponse
            val response = dataSource.getMenusList()
            // memverify data apakah data sudah terpanggil
            coVerify { service.getMenusList() }
            // mencocok kan data
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            // memanggil data
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            // memanggil data
            coEvery { service.createOrder(mockRequest) }
            // memverify data apakah data sudah terpanggil
            coVerify { service.createOrder(any()) }
            // mencocok kan data
            assertEquals(response, mockResponse)
        }
    }
}
