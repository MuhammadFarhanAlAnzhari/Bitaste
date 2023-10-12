package com.zhari.bitaste.data.dummy

import com.zhari.bitaste.model.Category

interface DummyCategoryDataSource {
    fun getMenuCategory(): List<Category>
}

class DummyCategoryDataSourceImpl() : DummyCategoryDataSource {
    override fun getMenuCategory(): List<Category> =
        listOf(
            Category(
                name = "Rice",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/Bitaste/master/app/src/main/res/drawable/ic_nasi.png"
            ),
            Category(
                name = "Noodle",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/Bitaste/master/app/src/main/res/drawable/ic_mie.png"
            ),
            Category(
                name = "Snack",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/Bitaste/master/app/src/main/res/drawable/ic_snack.png"
            ),
            Category(
                name = "Drink",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/Bitaste/master/app/src/main/res/drawable/ic_minuman.png"
            )
        )
}