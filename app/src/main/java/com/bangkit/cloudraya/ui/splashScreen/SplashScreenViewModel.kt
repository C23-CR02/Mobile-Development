package com.bangkit.cloudraya.ui.splashScreen

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class SplashScreenViewModel(private val cloudRepository: CloudRepository): ViewModel() {
    fun getOnboarding():Boolean{
        return cloudRepository.getOnboarding()
    }

    fun getProject() : String? {
        return cloudRepository.getProject()
    }
}