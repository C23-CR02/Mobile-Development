package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class InsertResponse(

	@field:SerializedName("message")
	val message: String
)
