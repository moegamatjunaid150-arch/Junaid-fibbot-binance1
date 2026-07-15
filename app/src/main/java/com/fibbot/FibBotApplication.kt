package com.fibbot

import android.app.Application
import com.fibbot.di.DependencyInjection
import timber.log.Timber

class FibBotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize dependency injection
        DependencyInjection.init(this)
        
        Timber.d("FibBot Application initialized")
    }
}