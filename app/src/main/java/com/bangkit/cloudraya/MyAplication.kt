package com.bangkit.cloudraya

import android.app.Application
import com.example.storyapp.di.localModule
import com.example.storyapp.di.networkModule
import com.example.storyapp.di.repositoryModule
import com.example.storyapp.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyAplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyAplication)
            modules(
                networkModule, viewModule, repositoryModule, localModule
            )
        }
    }
}