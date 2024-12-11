package com.digitalsoch

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.digitalsoch.di.applicationModule
import com.digitalsoch.di.viewModel
import com.digitalsoch.di.viewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DigitalSochApplication : Application(), LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DigitalSochApplication)
            modules(listOf(applicationModule, viewModel, viewModelFactory))
        }
    }
}