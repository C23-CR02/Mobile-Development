package com.bangkit.cloudraya

import android.app.Application
import android.util.Log

import com.bangkit.cloudraya.di.*
import com.google.firebase.messaging.FirebaseMessaging
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
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    // Lakukan sesuatu dengan token, misalnya kirim ke server
                    Log.d("FCM Token", token ?: "Token retrieval failed")
                } else {
                    Log.e("FCM Token", "Token retrieval failed", task.exception)
                }
            }
    }
}