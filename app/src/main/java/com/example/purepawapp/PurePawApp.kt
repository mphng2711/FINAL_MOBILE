package com.example.purepawapp

import android.app.Application
import com.example.purepawapp.data.seed.AppBootstrap
import com.example.purepawapp.di.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurePawApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)

        if (BuildConfig.DEBUG) {
            CoroutineScope(Dispatchers.IO).launch {
                runCatching { AppBootstrap.runIfNeeded() }
            }
        }
    }
}
