package com.bangkit.cloudraya.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.cloudraya.database.CloudDatabase
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.*
import com.bangkit.cloudraya.network.ApiService
import com.bangkit.cloudraya.utils.BaseUrlInterceptor
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.json.JSONObject

class CloudRepository(
    private val apiService: ApiService,
    private val cloudDatabase: CloudDatabase,
    private val sharedPreferences: SharedPreferences,
    private val baseUrlInterceptor: BaseUrlInterceptor

) {
    fun setBaseUrl(url: String) {
        baseUrlInterceptor.setBaseUrl(url)
    }

    fun getVMList(token: String): LiveData<Event<VMListResponse>> = liveData(Dispatchers.IO) {
        emit(Event.Loading)
        try {
            val response = apiService.getVMList(token)
            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    emit(Event.Success(it))
                }
            } else {
                val error = response.errorBody()?.toString()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Event.Error(null, message))
                }
            }
        } catch (e: Exception) {
            emit(Event.Error(null, e.toString()))
        }
    }

    fun getVMDetail(token: String, id: Int): LiveData<Event<VMDetailResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getVMDetail(token, id)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                emit(Event.Error(null, e.toString()))
            }
        }

    fun vmAction(token: String, vmId: Int, request: String): LiveData<Event<VMActionResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val requestBody = JsonObject().apply {
                    addProperty("vm_id", vmId)
                    addProperty("request", request)
                    addProperty("release_ip", false)
                }
                val response = apiService.vmAction("application/json", token, requestBody)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getToken(request: JsonObject): LiveData<Event<TokenResponse>> = liveData(Dispatchers.IO) {
        emit(Event.Loading)
        try {
            val response = apiService.getToken("application/json", request)
            if (response.isSuccessful) {
                val data = response.body()
                if (data?.data != null) {
                    emit(Event.Success(data))
                } else {
                    emit(Event.Error(null, data?.message ?: "Data anda salah"))
                }
            } else {
                val error = response.errorBody()?.toString()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Event.Error(null, message))
                }
            }
        } catch (e: Exception) {
            emit(Event.Error(null, "App Key atau App Secret anda salah"))
        }
    }


    suspend fun insertSites(site: Sites) {
        withContext(Dispatchers.IO) {
            cloudDatabase.sitesDao().insertSites(site)
        }
    }

    suspend fun getSites(): List<Sites> {
        return withContext(Dispatchers.IO) {
            cloudDatabase.sitesDao().getAllSites()
        }
    }

    // Fungsi untuk SharedPreferencesEncrypted
    fun saveEncryptedValues(appKey: String, appSecret: String, token: String) {
        sharedPreferences.edit().putString("app_key", appKey).putString("app_secret", appSecret)
            .putString("token", token).apply()
    }


    fun saveList(key: String, list: List<Any>) {
        val gson = Gson()
        val json = gson.toJson(list)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getList(key: String): List<Any> {
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            val type = object : TypeToken<List<Any>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveOnboarding(onBoarding: Boolean) {
        sharedPreferences.edit().putBoolean("onboarding_complete", onBoarding).apply()
    }

    fun getOnboarding(): Boolean {
        return sharedPreferences.getBoolean("onboarding_complete", false)
    }

    fun getFCMToken(): String? {
        return sharedPreferences.getString("fcm_token", "")
    }

    // SiteList
    suspend fun deleteItemWithConfirmation(item: Sites) {
        cloudDatabase.sitesDao().deleteSite(item)
    }

    fun getDataGraph(vmId: String): LiveData<Event<DataGraphResponse>> = liveData(Dispatchers.IO) {
        emit(Event.Loading)
        try {
            setBaseUrl("https://backend-dot-mobile-notification-90a3a.et.r.appspot.com")
            val requestBody = JsonObject().apply {
                addProperty("vm_id", vmId)
            }
            val response = apiService.getDataGraph("application/json", requestBody)
            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    emit(Event.Success(it))
                }
            } else {
                val error = response.errorBody()?.toString()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Event.Error(null, message))
                }
            }
        } catch (e: Exception) {
            emit(Event.Error(null, e.toString()))
        }
    }

    fun getDataAnomaly(vmId: String): LiveData<Event<DataAnomalyResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                setBaseUrl("https://backend-dot-mobile-notification-90a3a.et.r.appspot.com")
                val requestBody = JsonObject().apply {
                    addProperty("vm_id", vmId)
                }
                val response = apiService.getDataAnomaly("application/json", requestBody)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))

                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                emit(Event.Error(null, e.toString()))
            }
        }

    fun insertToDatabase(request: JsonObject): LiveData<Event<InsertResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                setBaseUrl("https://backend-dot-mobile-notification-90a3a.et.r.appspot.com")
                val response = apiService.insertToDatabase("application/json", request)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getBackupList(token : String, siteUrl : String) : LiveData<Event<BackupListResponse>> =
        liveData(Dispatchers.IO){
            emit(Event.Loading)
            try {
                setBaseUrl(siteUrl)
                Log.d("Testing","token : $token")
                Log.d("Testing","site : $siteUrl")
                val response = apiService.getBackupList(token)
                Log.d("Testing","response : $response")
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                emit(Event.Error(null, e.toString()))
            }
        }

    fun deleteBackup (token: String, backupId: Int, vmId: Int): LiveData<Event<ActionBackupResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val requestBody = JsonObject().apply {
                    addProperty("vm_id", vmId)
                }
                val response = apiService.deleteBackup(token, backupId, requestBody)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                emit(Event.Error(null, e.toString()))
            }
        }

    fun restoreBackup (token: String, backupId: Int, vmId: Int): LiveData<Event<ActionBackupResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val requestBody = JsonObject().apply {
                    addProperty("vm_id", vmId)
                }
                val response = apiService.deleteBackup(token, backupId, requestBody)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                emit(Event.Error(null, e.toString()))
            }
        }
}