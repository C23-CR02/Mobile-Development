package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class VMActionResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
