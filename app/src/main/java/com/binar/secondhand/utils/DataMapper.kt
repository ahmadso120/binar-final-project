package com.binar.secondhand.utils

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.CategoryEntity
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse

object DataMapper {
    fun mapResponsesToBuyerProductEntities(input: List<BuyerProductResponse>): List<BuyerProductEntity> {
        val dataList = ArrayList<BuyerProductEntity>()
        input.map {
            val categories = ArrayList<CategoryEntity>()
            if (it.Categories?.isNotEmpty() == true) {
                it.Categories.map { categoryResponse ->
                    val category = CategoryEntity(
                        categoryResponse.id, categoryResponse.name
                    )
                    categories.add(category)
                }

            }
            val buyerProducts = BuyerProductEntity(
                it.basePrice,
                it.createdAt,
                it.description,
                it.id,
                it.imageName,
                it.imageUrl,
                it.location,
                it.name,
                it.status,
                it.updatedAt,
                it.userId,
                categories
            )
            dataList.add(buyerProducts)
        }
        return dataList
    }
}