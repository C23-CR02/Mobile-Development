package com.bangkit.cloudraya.ui.resources

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository


class ResourcesViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getVMList(token: String) = cloudRepository.getVMList(token)
    fun getListEncrypted(key: String): List<Any> {
        return cloudRepository.getList(key)
    }

}