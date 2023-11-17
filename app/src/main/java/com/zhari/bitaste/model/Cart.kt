package com.zhari.bitaste.model

data class Cart(
    var id: Int? = null,
    var menuId: Int? = null,
    var itemQuantity: Int = 0,
    val menuName: String,
    val menuPrice: Double,
    val menuImgUrl: String,
    var itemNotes: String? = null
)
