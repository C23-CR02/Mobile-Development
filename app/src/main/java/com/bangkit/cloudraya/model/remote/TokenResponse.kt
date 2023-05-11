package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: TokenData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class TokenData(

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,

	@field:SerializedName("bearer_token")
	val bearerToken: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("expired_at")
	val expiredAt: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
