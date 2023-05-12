package com.bangkit.cloudraya.di

import androidx.room.Room
import com.bangkit.cloudraya.database.CloudDatabase
import com.bangkit.cloudraya.network.ApiService
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val localModule = module {
    factory { get<CloudDatabase>().sitesDao() }
    single {
        Room.databaseBuilder(
            androidContext().applicationContext,
            CloudDatabase::class.java, "App.db"
        ).build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder().addInterceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .build()
            chain.proceed(requestHeaders)
        }
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.cloudraya.com/v1/api/gateway/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

