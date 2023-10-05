package com.bangkit.cloudraya.ui.menu.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.repository.CloudRepository

class SharedViewModel(private val repository: CloudRepository) : ViewModel() {
    private val _lastSelectedFragmentId = MutableLiveData<Int>()
    val lastSelectedFragmentId: LiveData<Int> get() = _lastSelectedFragmentId

    fun setLastSelectedFragmentId(fragmentId: Int) {
        _lastSelectedFragmentId.value = fragmentId
    }
}