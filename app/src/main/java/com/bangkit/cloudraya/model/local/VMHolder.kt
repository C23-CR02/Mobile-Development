package com.bangkit.cloudraya.model.local

import java.io.Serializable

class VMHolder : Serializable {
    var location: String = ""
    var packageId: Int = 0
    var hostname: String = ""
    var securityProfile: Int = 0
    var vpcNetworkId: Int = 0
    var publicIpId: String = ""
    var diskId: Int = 0
    var scheduleDate: String = ""
    var scheduleTime: String = ""
    var templateId: String = ""

    fun clearData() {
        location = ""
        packageId = 0
        hostname = ""
        securityProfile = 0
        vpcNetworkId = 0
        publicIpId = ""
        diskId = 0
        scheduleDate = ""
        scheduleTime = ""
        templateId = ""
    }
}