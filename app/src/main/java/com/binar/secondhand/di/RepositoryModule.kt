package com.binar.secondhand.di

import com.binar.secondhand.data.*
import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.remote.AccountRemoteDataSource
import com.binar.secondhand.data.source.remote.AuthRemoteDataSource
import com.binar.secondhand.data.source.remote.BuyerProductRemoteDataSource
import com.binar.secondhand.data.source.remote.SellerCategoryDataSource
import com.binar.secondhand.utils.AppExecutors
import org.koin.dsl.module

val repositoryModule = module {
    single { BuyerProductLocalDataSource(get()) }
    single { BuyerProductRemoteDataSource(get()) }
    single { AuthRemoteDataSource(get()) }

    single {AccountRemoteDataSource(get())}

    single { SellerCategoryDataSource(get()) }

    factory { AppExecutors() }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<BuyerRepository> { BuyerRepositoryImpl(get(), get(), get()) }

    single<AccountRepository>{AccountRepositoryImpl(get())}

    single<SellerCategoryRepository> { SellerCategoryRepositoryImpl(get()) }

}