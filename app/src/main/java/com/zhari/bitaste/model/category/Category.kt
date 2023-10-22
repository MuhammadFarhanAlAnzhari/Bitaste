package com.zhari.bitaste.model.category

import java.util.UUID

data class Category(
    val id: Int,
    val catName: String,
    val catImgSrc: String,
    val slug: String
)