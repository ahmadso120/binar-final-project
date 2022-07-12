package com.binar.secondhand.data.source.local.room

import androidx.room.*
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyerProductDao {
    @Transaction
    @Query("SELECT * FROM buyer_product ORDER BY buyer_product.buyerProductId DESC")
    fun getBuyerProducts(): Flow<List<BuyerProductEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBuyerProduct(data: List<BuyerProductEntity>)

    @Query("DELETE FROM buyer_product")
    suspend fun deleteAllBuyerProduct()
}