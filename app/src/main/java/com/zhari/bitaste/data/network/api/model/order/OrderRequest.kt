package com.zhari.bitaste.data.network.api.model.order


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class OrderRequest(
    @SerializedName("orders")
    val orderItemRequests: List<OrderItemRequest?>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("username")
    val username: String?
)