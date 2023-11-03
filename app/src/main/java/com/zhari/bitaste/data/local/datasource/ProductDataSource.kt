package com.zhari.bitaste.data.local.datasource

import com.zhari.bitaste.data.local.dao.MenuDao
import com.zhari.bitaste.data.local.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

interface ProductDataSource {
    fun getAllProducts(): Flow<List<MenuEntity>>
    fun getProductById(id: Int): Flow<MenuEntity>
    suspend fun insertProducts(product: List<MenuEntity>)
    suspend fun deleteProduct(product: MenuEntity): Int
    suspend fun updateProduct(product: MenuEntity): Int
}

class ProductDatabaseDataSource(private val productDao: MenuDao) : ProductDataSource {
    override fun getAllProducts(): Flow<List<MenuEntity>> {
        return productDao.getAllProducts()
    }

    override fun getProductById(id: Int): Flow<MenuEntity> {
        return productDao.getProductById(id)
    }

    override suspend fun insertProducts(product: List<MenuEntity>) {
        return productDao.insertProduct(product)
    }

    override suspend fun deleteProduct(product: MenuEntity): Int {
        return productDao.deleteProduct(product)
    }

    override suspend fun updateProduct(product: MenuEntity): Int {
        return productDao.updateProduct(product)
    }
}
