package com.zhari.bitaste.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
    val id: Int? = null,
    val name: String,
    val price: Double,
    val desc: String,
    val imgMenuUrl: String
) : Parcelable