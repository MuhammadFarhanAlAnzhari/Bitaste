package com.zhari.bitaste.data.repository

import com.zhari.bitaste.data.datasource.CategoryDataSource
import com.zhari.bitaste.data.datasource.ProductDataSource
import com.zhari.bitaste.model.Category
import com.zhari.bitaste.model.Menu

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface ProductRepository {
    fun getCategories(): List<Category>
    fun getProducts(): List<Menu>
}

class ProductRepositoryImpl(
    private val categoryDataSource: CategoryDataSource,
    private val productDataSource: ProductDataSource
) : ProductRepository {

    override fun getCategories(): List<Category> {
        return categoryDataSource.getProductCategory()
    }

    override fun getProducts(): List<Menu> {
        return productDataSource.getProductList()
    }
}