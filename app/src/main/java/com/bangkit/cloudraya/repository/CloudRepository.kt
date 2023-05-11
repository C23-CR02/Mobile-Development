package com.bangkit.cloudraya.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.cloudraya.database.CloudDatabase
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.model.remote.VMListResponse
import com.bangkit.cloudraya.network.ApiService
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CloudRepository(private val apiService: ApiService, private val cloudDatabase: CloudDatabase) {

    fun getVMList(): LiveData<Event<VMListResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getVMList("Bearer ")
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

    suspend fun insertSites(site : Sites){
        withContext(Dispatchers.IO){
            cloudDatabase.sitesDao().insertSites(site)

        }
    }

    suspend fun getSites() : List<Sites> {
        return withContext(Dispatchers.IO){
            cloudDatabase.sitesDao().getAllSites()
        }
    }

}