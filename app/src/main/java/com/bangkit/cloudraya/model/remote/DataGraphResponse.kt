package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class DataGraphResponse(

	@field:SerializedName("DataGraphResponse")
	val dataGraphResponse: List<DataGraphResponseItem?>? = null
)

data class DataGraphResponseItem(

	@field:SerializedName("Forecasts")
	val forecasts: String? = null,

	@field:SerializedName("Timestamp")
	val timestamp: String? = null,

	@field:SerializedName("Cost")
	val cost: String? = null,

	@field:SerializedName("Core")
	val core: String? = null
)
