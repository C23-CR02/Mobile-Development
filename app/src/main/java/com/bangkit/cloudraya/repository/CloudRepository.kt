package com.bangkit.cloudraya.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.VMListResponse
import com.bangkit.cloudraya.network.ApiService
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class CloudRepository(private val apiService: ApiService) {

    fun getVMList(): LiveData<Event<VMListResponse>> =
        liveData(Dispatchers.IO){
            emit(Event.Loading)
            try {
                val response = apiService.getVMList("Bearer ")
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