package com.bangkit.cloudraya.di

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.bangkit.cloudraya.database.CloudDatabase
import com.bangkit.cloudraya.network.ApiService
import com.bangkit.cloudraya.utils.BaseUrlInterceptor
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
        ).fallbackToDestructiveMigration()
            .build()
    }
}
val networkModule = module {
    single { provideBaseUrlInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideApiService(get()) }
}

private fun provideBaseUrlInterceptor(): BaseUrlInterceptor {
    return BaseUrlInterceptor("https://api.cloudraya.com")
}

private fun provideOkHttpClient(baseUrlInterceptor: BaseUrlInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(baseUrlInterceptor)
        .build()
}

private fun provideApiService(okHttpClient: OkHttpClient): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.cloudraya.com") // URL default, tidak digunakan saat melakukan permintaan
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}


val encryptionModule = module {
    single {
        val context: Context = androidContext()
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "encrypted_pref",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}


