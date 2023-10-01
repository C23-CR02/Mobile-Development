package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class StorageResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<StorageData>,

	@field:SerializedName("message")
	val message: String
)

data class Price(

	@field:SerializedName("idr")
	val idr: Idr,

	@field:SerializedName("usd")
	val usd: Usd
)

data class Usd(

	@field:SerializedName("price_off")
	val priceOff: Int,

	@field:SerializedName("price_on")
	val priceOn: Int
)

data class Idr(

	@field:SerializedName("price_off")
	val priceOff: Any,

	@field:SerializedName("price_on")
	val priceOn: Any
)

data class StorageData(

	@field:SerializedName("price")
	val price: Price,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("label")
	val label: String,

	@field:SerializedName("tags")
	val tags: String,

	@field:SerializedName("status")
	val status: String
)
