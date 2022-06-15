package com.binar.secondhand.utils

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.CategoryEntity
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse

object DataMapper {
    fun mapResponsesToEntities(input: List<BuyerProductResponse>): List<BuyerProductEntity> {
        val dataList = ArrayList<BuyerProductEntity>()
        input.map {
            val categoryList = ArrayList<CategoryEntity>()
            it.Categories?.map { categoryData ->
                val category = CategoryEntity(
                    categoryData.id,
                    categoryData.name
                )
                categoryList.add(category)
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
                categoryList
            )
            dataList.add(buyerProducts)
        }
        return dataList
    }
}