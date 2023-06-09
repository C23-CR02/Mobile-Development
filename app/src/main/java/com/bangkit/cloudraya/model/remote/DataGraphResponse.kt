package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class DataGraphResponse(

	val dataGraphResponse: List<DataGraphResponseItem>
)

data class DataGraphResponseItem(

	@field:SerializedName("Forecasts")
	val forecasts: String,

	@field:SerializedName("Timestamp")
	val timestamp: String,

	@field:SerializedName("Cost")
	val cost: String,

	@field:SerializedName("Core")
	val core: String
)
