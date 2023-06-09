package com.bangkit.cloudraya.ui.detailVM

import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.AnomalyResponse
import com.bangkit.cloudraya.model.remote.DataAnomalyResponse
import com.bangkit.cloudraya.model.remote.DataGraphResponse
import com.bangkit.cloudraya.model.remote.VMDetailResponse
import com.bangkit.cloudraya.repository.CloudRepository

class DetailVMViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    private val _vmDetail = MutableLiveData<Event<VMDetailResponse>>()
    val vmDetail: LiveData<Event<VMDetailResponse>> = _vmDetail
    fun getVMDetail(token: String, id: Int) {
        cloudRepository.getVMDetail(token, id).observeForever {
            _vmDetail.value = it
        }
    }

    fun vmAction(token: String, vmId: Int, request: String) =
        cloudRepository.vmAction(token, vmId, request)

    fun getListEncrypted(key: String): List<Any> {
        return cloudRepository.getList(key)
    }

    fun setBaseUrl(url: String) {
        cloudRepository.setBaseUrl(url)
    }

    private val _dataGraph = MutableLiveData<Event<DataGraphResponse>>()
    val dataGraph: LiveData<Event<DataGraphResponse>> = _dataGraph
    fun getDataGraph(vmId: String) {
        cloudRepository.getDataGraph(vmId).observeForever {
            _dataGraph.value = it
        }
    }

    private val _dataAnomaly = MutableLiveData<Event<DataAnomalyResponse>>()
    val dataAnomaly : LiveData<Event<DataAnomalyResponse>> = _dataAnomaly

    fun getDataAnomaly(vmid: String){
        cloudRepository.getDataAnomaly(vmid).observeForever {
            _dataAnomaly.value = it
        }
    }
}