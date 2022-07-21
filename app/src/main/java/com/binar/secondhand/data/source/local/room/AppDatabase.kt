package com.binar.secondhand.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.RemoteKeys
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse

@Database(
    entities = [BuyerProductEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buyerProductDao(): BuyerProductDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun remoteKeysDao(): RemoteKeysDao

}