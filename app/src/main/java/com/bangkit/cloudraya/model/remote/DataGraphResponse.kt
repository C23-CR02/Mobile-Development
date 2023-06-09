package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class DataGraphResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: List<Item>,

    @field:SerializedName("message")
    val message: String
)

data class Item(

    @field:SerializedName("Forecasts")
    val forecasts: String,

    @field:SerializedName("Timestamp")
    val timestamp: String,

    @field:SerializedName("Cost")
    val cost: String,

    @field:SerializedName("Core")
    val core: String
)
