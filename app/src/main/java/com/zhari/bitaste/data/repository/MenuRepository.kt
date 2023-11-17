package com.zhari.bitaste.data.repository

import com.zhari.bitaste.data.network.api.datasource.BitasteDataSource
import com.zhari.bitaste.data.network.api.model.category.toCategoryList
import com.zhari.bitaste.data.network.api.model.menu.toMenuList
import com.zhari.bitaste.model.category.Category
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.utils.ResultWrapper
import com.zhari.bitaste.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
    fun getMenus(category: String? = null): Flow<ResultWrapper<List<Menu>>>
    suspend fun getMenuList(): Flow<ResultWrapper<List<Menu>>>
}

class MenuRepositoryImpl(
    private val apiDataSource: BitasteDataSource
) : MenuRepository {

    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            apiDataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getMenus(category: String?): Flow<ResultWrapper<List<Menu>>> {
        return proceedFlow {
            apiDataSource.getProducts(category)?.data?.toMenuList() ?: emptyList()
        }
    }

    override suspend fun getMenuList(): Flow<ResultWrapper<List<Menu>>> {
        return proceedFlow { apiDataSource.getMenusList().data.toMenuList() }
    }
}
