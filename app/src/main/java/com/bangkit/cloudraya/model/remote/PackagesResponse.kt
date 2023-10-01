package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class PackagesResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<PackageItem>,

	@field:SerializedName("message")
	val message: String
)

data class PriceOnItem(

	@field:SerializedName("price")
	val price: Any,

	@field:SerializedName("currency")
	val currency: String
)

data class CreatedAt(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("timezone")
	val timezone: String,

	@field:SerializedName("timezone_type")
	val timezoneType: Int
)

data class UpdatedAt(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("timezone")
	val timezone: String,

	@field:SerializedName("timezone_type")
	val timezoneType: Int
)

data class PriceOffItem(

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("currency")
	val currency: String
)

data class PackageItem(

	@field:SerializedName("cpucore")
	val cpucore: Int,

	@field:SerializedName("root_disk_size")
	val rootDiskSize: String,

	@field:SerializedName("include_public_ip")
	val includePublicIp: String,

	@field:SerializedName("memory")
	val memory: Int,

	@field:SerializedName("bandwidth")
	val bandwidth: Int,

	@field:SerializedName("position_number")
	val positionNumber: Int,

	@field:SerializedName("price_off")
	val priceOff: List<PriceOffItem>,

	@field:SerializedName("created_at")
	val createdAt: CreatedAt,

	@field:SerializedName("cpu_speed")
	val cpuSpeed: Int,

	@field:SerializedName("price_on")
	val priceOn: List<PriceOnItem>,

	@field:SerializedName("updated_at")
	val updatedAt: UpdatedAt,

	@field:SerializedName("bandwidth_idr")
	val bandwidthIdr: Int,

	@field:SerializedName("creator_id")
	val creatorId: Int,

	@field:SerializedName("includ_tax")
	val includTax: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: String
)
