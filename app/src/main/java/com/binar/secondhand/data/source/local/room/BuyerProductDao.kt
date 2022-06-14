package com.binar.secondhand.data.source.local.room

import androidx.room.*
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyerProductDao {
    @Query("SELECT * FROM buyer_product")
    fun getBuyerProducts(): Flow<List<BuyerProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuyerProducts(data: List<BuyerProductEntity>)
}