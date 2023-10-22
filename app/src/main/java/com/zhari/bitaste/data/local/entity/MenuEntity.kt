package com.zhari.bitaste.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menus")
data class MenuEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "rating")
    val rating: Double,
    @ColumnInfo(name = "desc")
    val desc: String,
    @ColumnInfo(name = "menuImgUrl")
    val imgUrl: String
)
