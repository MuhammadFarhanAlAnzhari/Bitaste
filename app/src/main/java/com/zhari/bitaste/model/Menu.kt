package com.zhari.bitaste.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Menu(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    val desc: String,
    val menuImgUrl: String
): Parcelable

