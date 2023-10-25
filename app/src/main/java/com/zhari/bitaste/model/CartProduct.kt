package com.zhari.bitaste.model

import com.zhari.bitaste.model.product.Menu

data class CartProduct(
    val product: Menu,
    val cart: Cart
)
