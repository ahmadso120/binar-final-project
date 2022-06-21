package com.binar.secondhand.di

import com.binar.secondhand.data.source.remote.network.*

import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.utils.connection.HasInternetCapability
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://market-final-project.herokuapp.com/"

val networkModule = module {
    single { HasInternetCapability(androidContext()) }

    single { createOkHttpClient(get()) }
    factory { createAccessTokenInterceptor(get()) }
    factory { createConverterFactory() }
    factory { createService<AuthService>(get(), get()) }
    factory { createService<BuyerProductService>(get(), get()) }

    factory { createService<AccountSettingService>(get(), get()) }


    factory { createService<AccountService>(get(),get()) }
    factory { createService<NotificationService>(get(),get()) }
    factory { createService<SellerCategoryService>(get(), get()) }

}

private fun createOkHttpClient(accessTokenInterceptor: AccessTokenInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(createHttpLoggingInterceptor())
        .addInterceptor(accessTokenInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

private fun createHttpLoggingInterceptor(): Interceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private fun createAccessTokenInterceptor(
    appLocalData: AppLocalData
): AccessTokenInterceptor {
    return AccessTokenInterceptor(appLocalData)
}

private fun createConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

private inline fun <reified T> createService(
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory,
    baseUrl: String = BASE_URL
): T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()
        .create(T::class.java)
}