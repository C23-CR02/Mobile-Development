package com.bangkit.cloudraya

import android.app.Application
import com.example.storyapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyAplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyAplication)
            modules(
                networkModule, viewModule, repositoryModule, urlModule, localModule
            )
        }
    }
}