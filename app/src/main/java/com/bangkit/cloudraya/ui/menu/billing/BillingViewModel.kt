package com.bangkit.cloudraya.ui.menu.billing

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class BillingViewModel(private val cloudRepository: CloudRepository): ViewModel() {

    fun getProject() : String? = cloudRepository.getProject()
}