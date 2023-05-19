package com.bangkit.cloudraya.ui.DetailVM

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailVMViewModel(private val cloudRepository: CloudRepository): ViewModel() {
    fun getVMDetail(token: String, id: Int) = cloudRepository.getVMDetail(token, id)
    fun vmAction(token: String, vmId: Int, request: String) = cloudRepository.vmAction(token, vmId, request)

    suspend fun getToken() =
        withContext(Dispatchers.IO){
            cloudRepository.getSites()
        }

    fun getListEncrypted(key : String) : List<Any>{
        return cloudRepository.getList(key)
    }
}