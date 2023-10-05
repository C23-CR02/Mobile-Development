package com.bangkit.cloudraya

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.bangkit.cloudraya.di.dataModule
import com.bangkit.cloudraya.di.encryptionModule
import com.bangkit.cloudraya.di.localModule
import com.bangkit.cloudraya.di.networkModule
import com.bangkit.cloudraya.di.repositoryModule
import com.bangkit.cloudraya.di.viewModule
import com.bangkit.cloudraya.utils.NOTIFICATION_CHANNEL_ID
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext

class MyApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApplication)
            modules(
                networkModule,
                viewModule,
                repositoryModule,
                localModule,
                encryptionModule,
                dataModule,
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}