package com.yernazar.pidapplication

import android.app.Application
import com.yernazar.pidapplication.di.appModule
import com.yernazar.pidapplication.di.dataModule
import com.yernazar.pidapplication.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }
    }
}