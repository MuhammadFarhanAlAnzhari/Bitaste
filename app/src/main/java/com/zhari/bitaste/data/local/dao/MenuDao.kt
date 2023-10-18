package com.zhari.bitaste.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zhari.bitaste.data.local.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Query("SELECT * FROM MENUS")
    fun getAllProducts(): Flow<List<MenuEntity>>

    @Query("SELECT * FROM MENUS WHERE id == :id")
    fun getProductById(id: Int): Flow<MenuEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: kotlin.collections.List<com.zhari.bitaste.data.local.entity.MenuEntity?>)

    @Delete
    suspend fun deleteProduct(product: MenuEntity): Int

    @Update
    suspend fun updateProduct(product: MenuEntity): Int
}