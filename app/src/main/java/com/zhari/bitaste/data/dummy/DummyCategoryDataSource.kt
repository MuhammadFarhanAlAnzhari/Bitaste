package com.zhari.bitaste.data.dummy

import com.zhari.bitaste.model.Category

interface CategoryDataSource {
    fun getProductCategory(): List<Category>
}

class CategoryDataSourceImpl() : CategoryDataSource {
    override fun getProductCategory(): List<Category> =
        listOf(
            Category(
                name = "Rice",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/categories/nasi.png"
            ),
            Category(
                name = "Noodles",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/categories/mie.png"
            ),
            Category(
                name = "Snack",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/categories/snack.png"
            ),
            Category(
                name = "Drink",
                imgCategoryUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/categories/minuman.png"
            )
        )
}