package com.bangkit.cloudraya.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.cloudraya.database.CloudDatabase
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.ActionBackupResponse
import com.bangkit.cloudraya.model.remote.BackupConfigResponse
import com.bangkit.cloudraya.model.remote.BackupListResponse
import com.bangkit.cloudraya.model.remote.DataAnomalyResponse
import com.bangkit.cloudraya.model.remote.DataGraphResponse
import com.bangkit.cloudraya.model.remote.InsertResponse
import com.bangkit.cloudraya.model.remote.IpBasicResponse
import com.bangkit.cloudraya.model.remote.IpPrivateResponse
import com.bangkit.cloudraya.model.remote.IpPublicResponse
import com.bangkit.cloudraya.model.remote.IpVMOwn
import com.bangkit.cloudraya.model.remote.LocationResponse
import com.bangkit.cloudraya.model.remote.PackagesResponse
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
            e.printStackTrace()
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
                e.printStackTrace()
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getToken(appKey: String, appSecret: String): LiveData<Event<TokenResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val request = JsonObject().apply {
                    addProperty("app_key", appKey)
                    addProperty("secret_key", appSecret)
                }
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
                e.printStackTrace()
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
            e.printStackTrace()
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun insertToDatabase(appKey: String, appSecret: String): LiveData<Event<InsertResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                setBaseUrl("https://backend-dot-mobile-notification-90a3a.et.r.appspot.com")
                val request = JsonObject().apply {
                    addProperty("app_key", appKey)
                    addProperty("secret_key", appSecret)
                }
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getBackupList(token: String, siteUrl: String): LiveData<Event<BackupListResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                setBaseUrl(siteUrl)
                val response = apiService.getBackupList(token)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getIpPublic(token: String, siteUrl: String): LiveData<Event<IpPublicResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                setBaseUrl(siteUrl)
                val response = apiService.getIpPublic(token)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun deleteBackup(
        token: String,
        backupId: Int,
        vmId: Int
    ): LiveData<Event<ActionBackupResponse>> =
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun restoreBackup(
        token: String,
        backupId: Int,
        vmId: Int
    ): LiveData<Event<ActionBackupResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val requestBody = JsonObject().apply {
                    addProperty("vm_id", vmId)
                }
                val response = apiService.restoreBackup(token, backupId, requestBody)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getBackupConfig(token: String, vmId: Int): LiveData<Event<BackupConfigResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getBackupConfig(token, vmId)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun saveBackupConfig(
        token: String,
        vmId: Int,
        status: Int,
        days: Int,
        retentions: Int
    ): LiveData<Event<BackupListResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val requestBody = JsonObject().apply {
                    addProperty("vm_id", vmId)
                    addProperty("status", status)
                    addProperty("days", days)
                    addProperty("retentions", retentions)
                }
                val response = apiService.saveBackupConfig(token, requestBody)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getIpPrivate(
        token: String,
        siteUrl: String,
        vmId: Int
    ): LiveData<Event<IpPrivateResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                setBaseUrl(siteUrl)
                val request = JsonObject().apply {
                    addProperty("vm_id", vmId)
                }
                val response = apiService.getIpPrivate(token, "application/json", request)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun attachIpPublic(
        token: String,
        publicId: Int,
        privateIp: String,
        vmId: Int
    ): LiveData<Event<IpBasicResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val request = JsonObject().apply {
                    addProperty("public_ip_id", publicId)
                    addProperty("private_ip", privateIp)
                    addProperty("vm_id", vmId)
                }
                val response = apiService.attachPublicIp(token, "application/json", request)

                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                        Log.d("Attach", "success : $it")
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    Log.d("Attach", "error : $error")
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun releaseIp(
        token: String,
        ipId: String,
        loc: String,
        vmId: String
    ): LiveData<Event<IpBasicResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val request = JsonObject().apply {
                    addProperty("private_ip_id", ipId)
                    addProperty("location", loc)
                    addProperty("vm_id", vmId)
                }
                val response = apiService.releaseIpPrivate(token, "application/json", request)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun acquireIpPrivate(
        token: String,
        vmId: String
    ): LiveData<Event<IpBasicResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val request = JsonObject().apply {
                    addProperty("vm_id", vmId)
                }
                val response = apiService.acquireIpPrivate(token, "application/json", request)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun acquireIpPublic(
        token: String,
        vmId: String
    ): LiveData<Event<IpBasicResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val request = JsonObject().apply {
                    addProperty("vm_id", vmId)
                }
                val response = apiService.acquireIpPublic(token, "application/json", request)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getIpVMOwn(token: String, vmId: Int): LiveData<Event<IpVMOwn>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val request = JsonObject().apply {
                    addProperty("id", vmId)
                }
                val response = apiService.getIpVM(token, "application/json", request)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun detachIPPublic(
        token: String,
        ipId: Int,
        vmId: Int
    ): LiveData<Event<IpBasicResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val request = JsonObject().apply {
                    addProperty("public_ip_id", ipId)
                    addProperty("vm_id", vmId)
                }
                val response = apiService.detachPublicIP(token, "application/json", request)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getLocation(token: String): LiveData<Event<LocationResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getLocation(token)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getPackages(token: String): LiveData<Event<PackagesResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getPackages(token)
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
                e.printStackTrace()
                emit(Event.Error(null, e.toString()))
            }
        }

}