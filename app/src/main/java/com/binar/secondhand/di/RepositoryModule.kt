package com.binar.secondhand.di

import com.binar.secondhand.data.AuthRepository
import com.binar.secondhand.data.AuthRepositoryImpl
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.BuyerRepositoryImpl
import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.remote.AuthRemoteDataSource
import com.binar.secondhand.data.source.remote.BuyerProductRemoteDataSource
import com.binar.secondhand.utils.AppExecutors
import org.koin.dsl.module

val repositoryModule = module {
    single { BuyerProductLocalDataSource(get()) }
    single { BuyerProductRemoteDataSource(get()) }
    single { AuthRemoteDataSource(get()) }
    factory { AppExecutors() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<BuyerRepository> { BuyerRepositoryImpl(get(), get(), get()) }
}