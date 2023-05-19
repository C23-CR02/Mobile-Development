package com.bangkit.cloudraya.ui.SiteList

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.repository.CloudRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SiteListViewModel(private val cloudRepository: CloudRepository):ViewModel() {
    suspend fun getSites(): List<Sites> {
        return withContext(Dispatchers.IO) {
            cloudRepository.getSites()
        }
    }
    fun getDecrypted(value:String): String{
        return cloudRepository.getDecrypted(value)
    }

    fun getListEncrypted(key:String) : List<Any>{
        return cloudRepository.getList(key)
    }
}