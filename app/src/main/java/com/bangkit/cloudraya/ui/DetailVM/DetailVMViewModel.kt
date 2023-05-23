package com.bangkit.cloudraya.ui.DetailVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.VMDetailResponse
import com.bangkit.cloudraya.repository.CloudRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailVMViewModel(private val cloudRepository: CloudRepository): ViewModel() {
    private val _vmDetail = MutableLiveData<Event<VMDetailResponse>>()
    val vmDetail: LiveData<Event<VMDetailResponse>> = _vmDetail
    fun getVMDetail(token: String, id: Int){
        cloudRepository.getVMDetail(token, id).observeForever {
            _vmDetail.value = it
        }
    }

    fun vmAction(token: String, vmId: Int, request: String) = cloudRepository.vmAction(token, vmId, request)

    suspend fun getToken() =
        withContext(Dispatchers.IO){
            cloudRepository.getSites()
        }

    fun getListEncrypted(key : String) : List<Any>{
        return cloudRepository.getList(key)
    }
}