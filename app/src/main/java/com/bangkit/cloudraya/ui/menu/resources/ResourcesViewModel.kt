package com.bangkit.cloudraya.ui.menu.resources

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class ResourcesViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getProject() : String? = cloudRepository.getProject()

    fun getList(key : String) : List<Any> = cloudRepository.getList(key)
}