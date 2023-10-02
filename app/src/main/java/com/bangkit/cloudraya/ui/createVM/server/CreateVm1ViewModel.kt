package com.bangkit.cloudraya.ui.createVM.server

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class CreateVm1ViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getLocationList(token: String) =
        cloudRepository.getLocation(token)

    fun getTemplateList(token: String, location: String) =
        cloudRepository.getTemplate(token, location)
}