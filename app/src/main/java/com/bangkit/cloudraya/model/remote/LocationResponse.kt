package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class LocationResponse(

	@field:SerializedName("data")
	val data: LocationData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class IdItem(

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("disable_ip")
	val disableIp: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class LocationData(

	@field:SerializedName("id")
	val id: List<IdItem?>? = null,

	@field:SerializedName("us")
	val us: List<UsItem?>? = null
)

data class UsItem(

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("disable_ip")
	val disableIp: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
