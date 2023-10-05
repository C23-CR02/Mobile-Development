package com.bangkit.cloudraya.ui.detailVM.monitoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.DataAnomalyResponse
import com.bangkit.cloudraya.model.remote.DataGraphResponse
import com.bangkit.cloudraya.repository.CloudRepository

class MonitoringVMViewModel(private val cloudRepository: CloudRepository) : ViewModel()  {
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