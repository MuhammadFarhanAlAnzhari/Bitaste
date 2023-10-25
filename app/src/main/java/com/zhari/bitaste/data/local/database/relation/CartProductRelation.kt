/*
package com.zhari.bitaste.data.local.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.zhari.bitaste.data.local.entity.CartEntity
import com.zhari.bitaste.data.local.entity.MenuEntity

data class CartProductRelation(
    @Embedded
    val cart: CartEntity,
    @Relation(parentColumn = "product_id", entityColumn = "id")
    val menu: MenuEntity
)
*/
