package com.binar.secondhand.di

import androidx.room.Room
import com.binar.secondhand.data.source.local.room.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    factory { get<AppDatabase>().buyerProductDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "second_hand.db"
        ).fallbackToDestructiveMigration().build()
    }
}