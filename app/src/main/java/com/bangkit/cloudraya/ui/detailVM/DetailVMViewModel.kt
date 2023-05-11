package com.bangkit.cloudraya.ui.detailVM

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class DetailVMViewModel(private val cloudRepository: CloudRepository): ViewModel() {
    fun getVMList() = cloudRepository.getVMList()
}