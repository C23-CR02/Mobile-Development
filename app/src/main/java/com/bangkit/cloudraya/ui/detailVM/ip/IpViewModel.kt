package com.bangkit.cloudraya.ui.detailVM.ip

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.IpBasicResponse
import com.bangkit.cloudraya.model.remote.IpPrivateResponse
import com.bangkit.cloudraya.model.remote.IpPublicResponse
import com.bangkit.cloudraya.repository.CloudRepository

class IpViewModel(private val repository: CloudRepository) : ViewModel() {



    fun getIpPublic(token: String, siteUrl: String): LiveData<Event<IpPublicResponse>> =
        repository.getIpPublic(token, siteUrl)

    fun getIpPrivate(
        token: String,
        siteUrl: String,
        vmId: Int
    ): LiveData<Event<IpPrivateResponse>> = repository.getIpPrivate(token, siteUrl, vmId)


    fun deleteIpPrivate(
        token: String,
        ipId: String,
        loc: String,
        vmId: String
    ): LiveData<Event<IpBasicResponse>> = repository.releaseIp(token, ipId, loc, vmId)

    fun acquireIpPrivate(
        token: String,
        vmId: String
    ) : LiveData<Event<IpBasicResponse>> = repository.acquireIpPrivate(token,vmId)
}