/*
package com.zhari.bitaste.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zhari.bitaste.data.dummy.ProductDataSourceImpl
import com.zhari.bitaste.data.local.AppDatabase.Companion.getInstance
import com.zhari.bitaste.data.local.dao.CartDao
import com.zhari.bitaste.data.local.dao.MenuDao
import com.zhari.bitaste.data.local.entity.CartEntity
import com.zhari.bitaste.data.local.entity.MenuEntity
import com.zhari.bitaste.data.local.mapper.toProductEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(
    entities = [CartEntity::class, MenuEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): MenuDao

    companion object {
        private const val DB_NAME = "Bitaste.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseSeederCallback(context))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

class DatabaseSeederCallback(private val context: Context) : RoomDatabase.Callback() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            getInstance(context).productDao().insertProduct(prepopulateProducts())
            getInstance(context).cartDao().insertCarts(prepopulateCarts())
        }
    }

    private fun prepopulateProducts(): List<MenuEntity?> {
        return ProductDataSourceImpl().getProductList().toProductEntity()
    }

    private fun prepopulateCarts(): List<CartEntity> {
        return mutableListOf(
            CartEntity(
                id = 1,
                menuId = 1,
                itemNotes = "Barang yang fresh ya",
                itemQuantity = 3
            ),
            CartEntity(
                id = 2,
                menuId = 2,
                itemNotes = "Barang yang fresh yaaaaaa",
                itemQuantity = 6
            ),
        )
    }
}
*/
