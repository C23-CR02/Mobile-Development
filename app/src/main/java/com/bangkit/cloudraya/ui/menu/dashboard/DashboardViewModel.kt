package com.bangkit.cloudraya.ui.menu.dashboard

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class DashboardViewModel(private val cloudRepository: CloudRepository) : ViewModel() {
    fun getProject(): String? {
        return cloudRepository.getProject()
    }
}