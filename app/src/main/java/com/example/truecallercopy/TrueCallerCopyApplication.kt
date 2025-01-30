package com.example.truecallercopy

import android.app.Application
import com.example.truecallercopy.dependencyInjection.databaseModule
import com.example.truecallercopy.dependencyInjection.networkModule
import com.example.truecallercopy.dependencyInjection.repositoryModule
import com.example.truecallercopy.dependencyInjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class TrueCallerCopyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TrueCallerCopyApplication)
            modules(listOf(networkModule, databaseModule, viewModelModule, repositoryModule))
        }
    }
}