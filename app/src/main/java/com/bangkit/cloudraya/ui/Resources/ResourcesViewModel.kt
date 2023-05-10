package com.bangkit.cloudraya.ui.Resources

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository


class ResourcesViewModel(private val cloudRepository: CloudRepository): ViewModel() {
    fun print(msg : String){
        Log.d("ta",msg)
    }
}