package com.bangkit.cloudraya.ui.siteAdd

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.repository.CloudRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SiteAddViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getToken(request: JsonObject): LiveData<Event<TokenResponse>> =
        cloudRepository.getToken(request)

    suspend fun insertSites(site: Sites) {
        withContext(Dispatchers.IO) {
            cloudRepository.insertSites(site)
        }
    }

    fun saveEncrypted(appKey: String, appSecret: String, token: String) {
        cloudRepository.saveEncryptedValues(appKey, appSecret, token)
    }
    fun saveListEncrypted(key : String, list : List<String>){
        cloudRepository.saveList(key, list)
    }

    fun getListEncrypted(key : String){
        cloudRepository.getList(key)
    }

    fun setBaseUrl(url : String){
        cloudRepository.setBaseUrl(url)
    }

    fun getFCMToken() = cloudRepository.getFCMToken()

}