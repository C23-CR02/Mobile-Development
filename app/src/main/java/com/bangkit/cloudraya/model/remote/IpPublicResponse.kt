package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class IpPublicResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String
)

data class DataItem(

	@field:SerializedName("country_code")
	val countryCode: String,

	@field:SerializedName("public_ip")
	val publicIp: String,

	@field:SerializedName("is_primary")
	val isPrimary: Int,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("object_type")
	val objecttype: String,

	@field:SerializedName("array_lb")
	val arrayLb: String,

	@field:SerializedName("object_id")
	val objectid: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("object")
	val objek: String
)
