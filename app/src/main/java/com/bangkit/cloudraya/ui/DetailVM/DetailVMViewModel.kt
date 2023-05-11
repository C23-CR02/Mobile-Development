package com.bangkit.cloudraya.ui.DetailVM

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class DetailVMViewModel(private val cloudRepository: CloudRepository): ViewModel() {
    fun getVMDetail(token: String, id: Int) = cloudRepository.getVMDetail(token, id)
    fun vmAction(token: String, vmId: Int, request: String) = cloudRepository.vmAction(token, vmId, request)
}