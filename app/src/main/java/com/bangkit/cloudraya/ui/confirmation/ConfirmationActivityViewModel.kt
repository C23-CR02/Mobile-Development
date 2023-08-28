package com.bangkit.cloudraya.ui.confirmation

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class ConfirmationActivityViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun vmAction(token: String, vmId: Int, request: String) =
        cloudRepository.vmAction(token, vmId, request)

    fun setBaseUrl(url: String) {
        cloudRepository.setBaseUrl(url)
    }
}