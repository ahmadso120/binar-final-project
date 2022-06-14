package com.binar.secondhand.data.source.local

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.room.BuyerProductDao
import kotlinx.coroutines.flow.Flow

class BuyerProductLocalDataSource (
    private val buyerProductDao: BuyerProductDao
) {
    fun getBuyerProducts(): Flow<List<BuyerProductEntity>> =
        buyerProductDao.getBuyerProducts()

    suspend fun insertBuyerProducts(
        data: List<BuyerProductEntity>
    ) = buyerProductDao.insertBuyerProducts(data)
}