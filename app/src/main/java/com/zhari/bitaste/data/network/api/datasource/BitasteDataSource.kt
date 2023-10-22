package com.zhari.bitaste.data.network.api.datasource

import com.catnip.egroceries.data.network.api.model.order.OrderRequest
import com.catnip.egroceries.data.network.api.model.order.OrderResponse
import com.zhari.bitaste.data.network.api.model.menu.MenuResponse
import com.zhari.bitaste.data.network.api.service.RestaurantService
import com.zhari.bitaste.data.network.api.model.category.CategoriesResponse

interface BitasteDataSource {
    suspend fun getProducts(category: String?): MenuResponse?
    suspend fun getCategories(): CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class BitasteDataSourceImpl(private val service: RestaurantService) : BitasteDataSource {
    override suspend fun getProducts(category: String?): MenuResponse? {
        return category?.let { service.getMenus(it) }
    }

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }
}
