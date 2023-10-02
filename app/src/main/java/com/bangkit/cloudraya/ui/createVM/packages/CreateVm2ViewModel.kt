package com.bangkit.cloudraya.ui.createVM.packages

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class CreateVm2ViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getPackages(token: String) =
        cloudRepository.getPackages(token)
}