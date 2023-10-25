package com.zhari.bitaste.data.network.api.service

import com.zhari.bitaste.data.network.api.model.order.OrderRequest
import com.zhari.bitaste.data.network.api.model.order.OrderResponse
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.zhari.bitaste.BuildConfig
import com.zhari.bitaste.data.network.api.model.menu.MenuResponse
import com.zhari.bitaste.data.network.api.model.category.CategoriesResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RestaurantService {
    @GET("listmenu")
    suspend fun getMenus(@Query("c") category: String): MenuResponse
    @GET("listmenu")
    suspend fun getMenusList(): MenuResponse
    @GET("category")
    suspend fun getCategories(): CategoriesResponse
    @POST("order")
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse


    companion object {
        @JvmStatic
        operator fun invoke(chucker: ChuckerInterceptor): RestaurantService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chucker)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(
                RestaurantService::class.java)
        }
    }
}
