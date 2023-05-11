package com.example.storyapp.di

import com.bangkit.cloudraya.network.ApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//val localModule = module {
//    factory { get<StoryDatabase>().storyDao() }
//    single {
//        Room.databaseBuilder(
//            androidContext().applicationContext,
//            StoryDatabase::class.java, "App.db"
//        ).build()
//    }
//}

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

