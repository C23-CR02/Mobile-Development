package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class DataAnomalyResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: List<AnomalyResponse>,

    @field:SerializedName("message")
    val message: String
)

data class AnomalyResponse(
    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("cpu_used")
    val cpuUsed: String,

    @field:SerializedName("is_anomaly")
    val isAnomaly: Boolean
)

