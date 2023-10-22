package com.zhari.bitaste.model.product

import android.os.Parcelable
import com.zhari.bitaste.data.local.entity.CartEntity
import com.zhari.bitaste.data.local.mapper.toCart
import com.zhari.bitaste.data.network.api.model.menu.MenuItemResponse
import com.zhari.bitaste.data.network.api.model.menu.MenuResponse
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Menu(
    val id: Int,
    val restaurantAddress: String,
    val detail: String,
    val price: Double,
    val formattedPrice: String,
    val imageUrl: String,
    val name: String
) : Parcelable
