package com.bangkit.cloudraya.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.cloudraya.ui.detailVM.FragmentBackup
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import com.bangkit.cloudraya.ui.detailVM.monitoring.FragmentMonitoringVM

class DetailMenuAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    private var token: String? = null
    private var vm_id: Int? = null

    fun setValue(token: String, vm_id: Int){
        this.token = token
        this.vm_id = vm_id
    }

    override fun getItemCount() = FragmentDetailVM.TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment
        when (position){
            0 -> {
                fragment = FragmentMonitoringVM()
                fragment.arguments = Bundle().apply {
                    putInt(FragmentDetailVM.ARG_VM_ID, vm_id!!)
                }
            }
            1 -> {
                fragment = FragmentBackup()
                fragment.arguments = Bundle().apply {
                    putString(FragmentDetailVM.ARG_TOKEN, token)
                    putInt(FragmentDetailVM.ARG_VM_ID, vm_id!!)
                }
            }
            else -> {
                fragment = FragmentMonitoringVM()
                    fragment.arguments = Bundle().apply {
                    putInt(FragmentDetailVM.ARG_VM_ID, vm_id!!)
                }
            }
        }
        return fragment
    }
}