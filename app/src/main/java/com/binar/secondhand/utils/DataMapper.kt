package com.binar.secondhand.utils

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.CategoryBuyerProductCrossRef
import com.binar.secondhand.data.source.local.entity.CategoryEntity
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse

object DataMapper {
    fun mapResponsesToBuyerProductEntities(input: List<BuyerProductResponse>): List<BuyerProductEntity> {
        val dataList = ArrayList<BuyerProductEntity>()
        input.map {
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
                it.userId
            )
            dataList.add(buyerProducts)
        }
        return dataList
    }

    fun mapResponsesToCategoryEntities(input: List<BuyerProductResponse>): List<CategoryEntity> {
        val categoryList = ArrayList<CategoryEntity>()
        input.map {
            it.Categories?.map { categoryData ->
                val category = CategoryEntity(
                    categoryData.id,
                    categoryData.name
                )
                categoryList.add(category)
            }
        }
        return categoryList
    }

    fun mapResponsesToCategoryBuyerProductCrossRef(
        input: List<BuyerProductResponse>
    ): List<CategoryBuyerProductCrossRef> {
        val dataList = ArrayList<CategoryBuyerProductCrossRef>()
        input.map {
            it.Categories?.map { category ->
                val data = CategoryBuyerProductCrossRef(
                    it.id,
                    category.id
                )
                dataList.add(data)
            }
        }
        return dataList
    }
}