package com.bangkit.cloudraya.model.local

import com.google.gson.annotations.SerializedName

data class VMActionRequest(
    @SerializedName("vm_id") val vm_id : String,
    @SerializedName("request") val request : String,
    @SerializedName("release_ip") val release_ip : Boolean
)
