package com.example.swipeassignment.application

import android.app.Application
import com.example.swipeassignment.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class ProductApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ProductApplication)
            modules(appModule)
        }
    }
}