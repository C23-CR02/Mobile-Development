package com.bangkit.cloudraya.ui.siteList

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.repository.CloudRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SiteListViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    suspend fun getSites(): List<Sites> {
        return withContext(Dispatchers.IO) {
            cloudRepository.getSites()
        }
    }

    fun setBaseUrl(url: String) {
        cloudRepository.setBaseUrl(url)
    }

    suspend fun removeSite(site: Sites) {
        return withContext(Dispatchers.IO) {
            cloudRepository.deleteItemWithConfirmation(site)
        }
    }

    fun setProject(key : String){
        cloudRepository.setProject(key)
    }
    fun getProject() : String? = cloudRepository.getProject()
    fun getList(key : String) : List<Any> = cloudRepository.getList(key)
}