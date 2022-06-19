package com.binar.secondhand.data.source.local

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.BuyerProductWithCategories
import com.binar.secondhand.data.source.local.entity.CategoryBuyerProductCrossRef
import com.binar.secondhand.data.source.local.entity.CategoryEntity
import com.binar.secondhand.data.source.local.room.BuyerProductDao
import kotlinx.coroutines.flow.Flow

class BuyerProductLocalDataSource (
    private val buyerProductDao: BuyerProductDao
) {
    fun getBuyerProducts(): Flow<List<BuyerProductWithCategories>> =
        buyerProductDao.getBuyerProducts()

    suspend fun insertCategoryBuyerProductCrossRef(
        categoryBuyerProductCrossRef: List<CategoryBuyerProductCrossRef>
    ) = buyerProductDao.insertCategoryBuyerProductCrossRef(categoryBuyerProductCrossRef)

    suspend fun insertCategories(data: List<CategoryEntity>)
        = buyerProductDao.insertCategory(data)

    suspend fun insertBuyerProduct(data: List<BuyerProductEntity>) =
        buyerProductDao.insertBuyerProduct(data)

    suspend fun deleteAllCategoryBuyerProductCrossRef() =
        buyerProductDao.deleteAllCategoryBuyerProductCrossRef()

    suspend fun deleteAllBuyerProduct() =
        buyerProductDao.deleteAllBuyerProduct()

    suspend fun deleteAllCategory() =
        buyerProductDao.deleteAllCategory()
}