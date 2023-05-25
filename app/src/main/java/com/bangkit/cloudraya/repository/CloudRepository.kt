package com.bangkit.cloudraya.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.cloudraya.database.CloudDatabase
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.model.remote.VMActionResponse
import com.bangkit.cloudraya.model.remote.VMDetailResponse
import com.bangkit.cloudraya.model.remote.VMListResponse
import com.bangkit.cloudraya.network.ApiService
import com.bangkit.cloudraya.utils.BaseUrlInterceptor
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CloudRepository(
    private val apiService: ApiService,
    private val cloudDatabase: CloudDatabase,
    private val sharedPreferences: SharedPreferences,
    private val baseUrlInterceptor: BaseUrlInterceptor

) {
    fun setBaseUrl(url : String){
        Log.d("Cloud Repository",url)
        baseUrlInterceptor.setBaseUrl(url)
        Log.d("Cloud Repository","Sukses ?")
    }

    fun getVMList(token: String): LiveData<Event<VMListResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getVMList(token)
                Log.d("Repository","Token : $token")

                Log.d("Repository","response : $response.toString()")
                Log.d("Repository " , response.body().toString())
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("Success", data.toString())
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    Log.d("Error", response.body()?.message!!)
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
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
                    Log.d("Success", data.toString())
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    Log.d("Error", response.body()?.message!!)
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
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

    fun getToken(request: JsonObject): LiveData<Event<TokenResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getToken("application/json", request)
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
    fun saveEncryptedValues(appKey: String, appSecret: String,token : String) {
        sharedPreferences.edit()
            .putString("app_key", appKey)
            .putString("app_secret", appSecret)
            .putString("token" , token)
            .apply()
    }


    fun saveList(key: String, list: List<Any>) {
        val gson = Gson()
        val json = gson.toJson(list)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getList(key: String) : List<Any> {
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            val type = object : TypeToken<List<Any>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}