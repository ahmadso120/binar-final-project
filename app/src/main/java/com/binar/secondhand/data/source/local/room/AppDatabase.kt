package com.binar.secondhand.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity

@Database(
    entities = [BuyerProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buyerProductDao(): BuyerProductDao
}