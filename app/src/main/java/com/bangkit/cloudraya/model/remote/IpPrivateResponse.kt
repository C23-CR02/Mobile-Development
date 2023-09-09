package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class IpPrivateResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<IpPrivateItem>,

	@field:SerializedName("message")
	val message: String? = null
)

data class IpPrivateItem(

	@field:SerializedName("ip_address")
	val ipAddress: String? = null,

	@field:SerializedName("is_default")
	val isDefault: Boolean? = null,

	@field:SerializedName("private_ip_id")
	val id: String? = null,

	@field:SerializedName("is_used")
	val isUsed: String? = null
)
