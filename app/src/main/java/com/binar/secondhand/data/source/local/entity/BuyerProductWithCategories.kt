package com.binar.secondhand.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BuyerProductWithCategories(
    @Embedded val buyerProductEntity: BuyerProductEntity,
    @Relation(
        parentColumn = "buyerProductId",
        entity = CategoryEntity::class,
        entityColumn = "categoryId",
        associateBy = Junction(
            value = CategoryBuyerProductCrossRef::class,
            parentColumn = "bId",
            entityColumn = "cId"
        )
    )
    val categories: List<CategoryEntity>
)