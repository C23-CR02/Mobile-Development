package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class IpBasicResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: BasicResponse? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class BasicResponse(

	@field:SerializedName("jobid")
	val jobid: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
