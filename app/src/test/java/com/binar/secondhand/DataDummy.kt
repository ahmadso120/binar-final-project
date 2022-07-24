package com.binar.secondhand

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.CategoryEntity

object DataDummy {
    fun generateDummyProductResponse(): List<BuyerProductEntity>{
        val items: MutableList<BuyerProductEntity> = arrayListOf()
        val categories: MutableList<CategoryEntity> = arrayListOf()

        for (i in 1 .. 4) {
            val category = CategoryEntity(
                i, "Category $i"
            )
            categories.add(category)
        }

        for (i in 0 .. 100) {
            val product = BuyerProductEntity(
                5000 + i,
                "2000-01-01T00:00:00.000Z",
                "Product $i",
                i.toString(),
                "PR-1654962957757-sepatu.jpg",
                "https://firebasestorage.googleapis.com/v0/b/market-final-project.appspot.com/o/products%2FPR-1654962957757-sepatu.jpg?alt=media",
                "Bandung",
                "Product $i",
                "available",
                "2000-01-01T00:00:00.000Z",
                i,
                categories
            )
            items.add(product)
        }

        return items
    }
}