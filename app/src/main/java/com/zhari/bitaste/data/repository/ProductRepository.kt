package com.zhari.bitaste.data.repository

import com.zhari.bitaste.data.dummy.CategoryDataSource
import com.zhari.bitaste.data.local.datasource.ProductDataSource
import com.zhari.bitaste.data.local.datasource.ProductDatabaseDataSource
import com.zhari.bitaste.data.local.mapper.toProductList
import com.zhari.bitaste.model.Category
import com.zhari.bitaste.model.Menu
import com.zhari.bitaste.utils.ResultWrapper
import com.zhari.bitaste.utils.proceed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface ProductRepository {
    fun getCategories(): List<Category>
    fun getProducts(): Flow<ResultWrapper<List<Menu>>>
}

class ProductRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val dummyCategoryDataSource: CategoryDataSource
) : ProductRepository {

    override fun getCategories(): List<Category> {
        return dummyCategoryDataSource.getProductCategory()
    }

    override fun getProducts(): Flow<ResultWrapper<List<Menu>>> {
        return productDataSource.getAllProducts().map {
            proceed { it.toProductList() //Product Entity to Product
            }
        }.onStart { //onStart is the first one of data running
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }
}