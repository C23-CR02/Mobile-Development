package com.bangkit.cloudraya.ui.resources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.repository.CloudRepository


class VMListViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getVMList(token: String) = cloudRepository.getVMList(token)
    fun getListEncrypted(key: String): List<Any> {
        return cloudRepository.getList(key)
    }

    fun saveListEncrypted(key: String, list: List<String>) {
        cloudRepository.saveList(key, list)
    }

    fun getToken(appKey: String, appSecret: String): LiveData<Event<TokenResponse>> =
        cloudRepository.getToken(appKey, appSecret)

    fun setBaseUrl(url: String) {
        Log.d("Testing", "Url : $url")
        cloudRepository.setBaseUrl(url)
    }

}