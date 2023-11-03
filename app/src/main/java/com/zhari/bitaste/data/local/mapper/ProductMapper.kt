/*
package com.zhari.bitaste.data.local.mapper

import com.zhari.bitaste.data.local.entity.MenuEntity
import com.zhari.bitaste.model.product.Menu

fun MenuEntity?.toProduct() = Menu(
    id = this?.id ?: 0,
    menuImgUrl = this?.imgUrl.orEmpty(),
    rating = this?.rating ?: 0.0,
    name = this?.name.orEmpty(),
    price = this?.price ?: 0.0,
    desc = this?.desc.orEmpty(),
)

fun Menu?.toProductEntity() = MenuEntity(
    id = this?.id,
    imgUrl = this?.menuImgUrl.orEmpty(),
    rating = this?.rating ?: 0.0,
    name = this?.name.orEmpty(),
    price = this?.price ?: 0.0,
    desc = this?.desc.orEmpty(),
)

fun List<MenuEntity?>.toProductList() = this.map { it.toProduct() }
fun List<Menu?>.toProductEntity() = this.map { it.toProductEntity() }


*/
/*fun MenuEntity?.toProduct() = Menu(
    id = this?.id?.toInt() ?: 0,
    menuImgUrl = this?.imgUrl.orEmpty(),
    rating = this?.rating ?: 0.0,
    name = this?.name.orEmpty(),
    price = this?.price?.toDouble() ?: 0.0,
    desc = this?.desc.orEmpty(),
)

fun Menu?.toProductEntity() = MenuEntity(
    id = this?.id?.toString(),
    imgUrl = this?.menuImgUrl.orEmpty(),
    rating = this?.rating ?: 0.0,
    name = this?.name.orEmpty(),
    price = this?.price ?: 0.0,
    desc = this?.desc.orEmpty(),
)

fun List<MenuEntity?>.toProductList() = this.map { it?.toProduct() }
fun List<Menu?>.toProductEntity() = this.map { it?.toProductEntity() }*/
