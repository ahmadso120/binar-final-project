package com.binar.secondhand

import android.app.Application
import com.binar.secondhand.di.*
import com.binar.secondhand.utils.ReleaseTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class SecondHandApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@SecondHandApplication)
            modules(
                listOf(
                    storageModule,
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        GlideApp.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        GlideApp.get(this).trimMemory(level)
    }
}