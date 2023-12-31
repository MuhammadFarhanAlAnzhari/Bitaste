package com.zhari.bitaste.data.repository

import com.zhari.bitaste.data.local.datasource.CartDataSource
import com.zhari.bitaste.data.local.entity.CartEntity
import com.zhari.bitaste.data.local.mapper.toCartEntity
import com.zhari.bitaste.data.local.mapper.toCartList
import com.zhari.bitaste.data.network.api.datasource.BitasteDataSource
import com.zhari.bitaste.data.network.api.model.order.OrderItemRequest
import com.zhari.bitaste.data.network.api.model.order.OrderRequest
import com.zhari.bitaste.model.Cart
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.utils.ResultWrapper
import com.zhari.bitaste.utils.proceed
import com.zhari.bitaste.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepository {
    fun getCartList(): Flow<ResultWrapper<Pair<List<Cart>, Double>>>
    suspend fun createCart(menu: Menu, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
    suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>>
}

class CartRepositoryImpl(
    private val dataSource: CartDataSource,
    private val restaurantApiDataSource: BitasteDataSource
) : CartRepository {

    override fun getCartList(): Flow<ResultWrapper<Pair<List<Cart>, Double>>> {
        return dataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf {
                        val pricePerItem = it.menuPrice
                        val quantity = it.itemQuantity
                        pricePerItem * quantity
                    }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == true) {
                    ResultWrapper.Empty(it.payload)
                } else {
                    it
                }
            }.catch {
                emit(ResultWrapper.Error(Exception(it)))
            }.onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun createCart(
        menu: Menu,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return menu.id?.let { menuId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(
                        menuId = menuId,
                        itemQuantity = totalQuantity,
                        menuImgUrl = menu.imageUrl,
                        menuName = menu.name,
                        menuPrice = menu.price
                    )
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Product ID not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteAll() {
        return dataSource.deleteAll()
    }

    override suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = items.map {
                OrderItemRequest(
                    it.itemNotes,
                    it.menuPrice.toInt(),
                    it.menuName,
                    it.itemQuantity
                )
            }

            val orderRequest = OrderRequest(
                orderItems,
                total = null,
                username = null
            )
            restaurantApiDataSource.createOrder(orderRequest).status == true
        }
    }
}
