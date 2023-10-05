package com.bangkit.cloudraya.ui.menu.networking

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class NetworkingViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getProject() : String? = cloudRepository.getProject()

}