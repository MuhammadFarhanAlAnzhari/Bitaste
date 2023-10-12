package com.zhari.bitaste.model

import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val imgCategoryUrl: String,
    val name: String
)