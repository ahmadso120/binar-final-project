package com.binar.secondhand.di

import androidx.room.Room
import com.binar.secondhand.BuildConfig
import com.binar.secondhand.data.AuthRepository
import com.binar.secondhand.data.AuthRepositoryImpl
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.BuyerRepositoryImpl
import com.binar.secondhand.data.source.local.BuyerLocalDataSource
import com.binar.secondhand.data.source.local.room.AppDatabase
import com.binar.secondhand.data.source.remote.AuthRemoteDataSource
import com.binar.secondhand.data.source.remote.BuyerRemoteDataSource
import com.binar.secondhand.data.source.remote.network.AccessTokenInterceptor
import com.binar.secondhand.data.source.remote.network.ApiService
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.storage.SharedPreferencesStorage
import com.binar.secondhand.storage.Storage
import com.binar.secondhand.utils.AppExecutors
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createOkHttpClient(appLocalData: AppLocalData): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    })
        .connectTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
    val token = appLocalData.getAccessToken
    if (token != null) {
        clientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("access_token", token)
                .method(original.method, original.body)
            chain.proceed(requestBuilder.build())
        }.build()
    }else{
        clientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method(original.method, original.body)
            chain.proceed(requestBuilder.build())
        }.build()
    }
    return clientBuilder.build()
}

val databaseModule = module {
    factory { get<AppDatabase>().buyerProductDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "second_hand.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single { AccessTokenInterceptor(get()) }
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .addInterceptor(AccessTokenInterceptor(get()))
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .build()


//        createOkHttpClient(get())

    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://market-final-project.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { BuyerLocalDataSource(get()) }
    single { BuyerRemoteDataSource(get()) }
    single { AuthRemoteDataSource(get()) }
    factory { AppExecutors() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<BuyerRepository> { BuyerRepositoryImpl(get(), get(), get()) }
}

val storageModule = module {
    single { AppLocalData(get()) }
    single<Storage> { SharedPreferencesStorage(androidContext()) }
}