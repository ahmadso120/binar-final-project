package com.binar.secondhand.di

import com.binar.secondhand.data.*
import com.binar.secondhand.data.source.NotificatioRepository
import com.binar.secondhand.data.source.NotificationRepositoryImpl
import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.remote.*

import com.binar.secondhand.utils.AppExecutors
import org.koin.dsl.module

val repositoryModule = module {
    single { BuyerProductLocalDataSource(get()) }
    single { BuyerProductRemoteDataSource(get()) }
    single { AuthRemoteDataSource(get()) }
    single { AccSettDataSource(get()) }
    single { AccountRemoteDataSource(get()) }
    single { NotificationRemoteDataSource(get()) }
    single { SellerCategoryDataSource(get()) }
    single { SellerOrderRemoteDataSource(get()) }

    factory { AppExecutors() }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<BuyerRepository> { BuyerRepositoryImpl(get(), get(), get(), get()) }
    single<AccSettRepo> { AccSettRepoImpl(get()) }
    single<AccountRepository>{AccountRepositoryImpl(get())}
    single<NotificatioRepository> {NotificationRepositoryImpl(get())  }
    single<SellerCategoryRepository> { SellerCategoryRepositoryImpl(get()) }
    single<SellerOrderRepository> { SellerOrderRepositoryImpl(get()) }
}