package com.bangkit.cloudraya.ui.onboarding

import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class OnBoardingViewModel(private val cloudRepository: CloudRepository): ViewModel() {
    fun setOnBoarding(completed : Boolean){
        cloudRepository.saveOnboarding(completed)
    }
}