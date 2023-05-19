package com.bangkit.cloudraya

import android.app.Application

import com.bangkit.cloudraya.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApplication)
            modules(
                networkModule, viewModule, repositoryModule, localModule, encryptionModule
            )
        }
    }
}