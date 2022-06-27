package com.binar.secondhand.utils

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse

object DataMapper {
    fun mapResponsesToBuyerProductEntities(input: List<BuyerProductResponse>): List<BuyerProductEntity> {
        val dataList = ArrayList<BuyerProductEntity>()
        input.map {
            var category = ""
            if (it.Categories?.isNotEmpty() == true) {
                category = it.Categories[0].name
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
                category
            )
            dataList.add(buyerProducts)
        }
        return dataList
    }
}