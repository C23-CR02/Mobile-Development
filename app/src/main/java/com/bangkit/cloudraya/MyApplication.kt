package com.bangkit.cloudraya

import android.app.Application
import android.content.SharedPreferences
import android.util.Log

import com.bangkit.cloudraya.di.*
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext

class MyApplication : Application(), KoinComponent {
    private val sharedPreferences: SharedPreferences by inject()
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApplication)
            modules(
                networkModule, viewModule, repositoryModule, localModule, encryptionModule, dataModule
            )
        }
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    saveFCMToken(token)
                    Log.d("FCM Token", token ?: "Token retrieval failed")
                } else {
                    Log.e("FCM Token", "Token retrieval failed", task.exception)
                }
            }
    }

    private fun saveFCMToken(token: String){
        val editor = sharedPreferences.edit()
        editor.putString("fcm_token", token)
        editor.apply()
    }
}