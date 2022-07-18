package com.binar.secondhand.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.*
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity

@Dao
interface BuyerProductDao {
    @Query("SELECT * FROM buyer_product")
    fun getBuyerProducts(): PagingSource<Int, BuyerProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuyerProduct(data: List<BuyerProductEntity>)

    @Query("DELETE FROM buyer_product")
    suspend fun deleteAllBuyerProduct()
}