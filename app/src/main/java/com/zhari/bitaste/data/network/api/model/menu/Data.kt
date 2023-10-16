package com.zhari.bitaste.data.network.api.model.menu


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Data(
    @SerializedName("alamat_resto")
    val address: String?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("harga")
    val price: Int?,
    @SerializedName("harga_format")
    val formatPrice: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("nama")
    val name: String?
)