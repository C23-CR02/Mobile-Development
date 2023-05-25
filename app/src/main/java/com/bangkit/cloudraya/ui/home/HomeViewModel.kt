package com.bangkit.cloudraya.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.repository.CloudRepository
import com.google.gson.JsonObject

class HomeViewModel(private val cloudRepository: CloudRepository): ViewModel() {

    fun saveListEncrypted(key : String, list : List<String>){
        cloudRepository.saveList(key, list)
    }
    fun getListEncrypted(key : String) : List<Any>{
        return cloudRepository.getList(key)
    }
    fun getToken(request: JsonObject): LiveData<Event<TokenResponse>> =
        cloudRepository.getToken(request)
}