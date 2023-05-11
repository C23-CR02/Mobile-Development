package com.bangkit.cloudraya.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.VMActionResponse
import com.bangkit.cloudraya.model.remote.VMDetailResponse
import com.bangkit.cloudraya.model.remote.VMListResponse
import com.bangkit.cloudraya.network.ApiService
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class CloudRepository(private val apiService: ApiService) {

    fun getVMList(token: String): LiveData<Event<VMListResponse>> =
        liveData(Dispatchers.IO){
            emit(Event.Loading)
            try {
                val response = apiService.getVMList(token)
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                }else{
                    val error = response.errorBody()?.toString()
                    if (error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            }catch (e: Exception){
                emit(Event.Error(null, e.toString()))
            }
        }

    fun getVMDetail(token: String, id: Int): LiveData<Event<VMDetailResponse>> =
        liveData(Dispatchers.IO){
            emit(Event.Loading)
            try {
                val response = apiService.getVMDetail(token, id)
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                }else{
                    val error = response.errorBody()?.toString()
                    if (error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            }catch (e: Exception){
                emit(Event.Error(null, e.toString()))
            }
        }

    fun vmAction(token: String, vmId: Int, request: String): LiveData<Event<VMActionResponse>> =
        liveData(Dispatchers.IO){
            emit(Event.Loading)
            try {
                val requestBody = mapOf(
                    "vm_id" to vmId,
                    "request" to request,
                    "release_ip" to false
                )
                val response = apiService.vmAction("application/json", token, requestBody)
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                }else{
                    val error = response.errorBody()?.toString()
                    if (error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            }catch (e: Exception){
                emit(Event.Error(null, e.toString()))
            }
        }
}