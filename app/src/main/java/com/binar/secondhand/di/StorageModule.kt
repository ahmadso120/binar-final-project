package com.binar.secondhand.di

import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.storage.SharedPreferencesStorage
import com.binar.secondhand.storage.Storage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single { AppLocalData(get()) }
    single<Storage> { SharedPreferencesStorage(androidContext()) }
}