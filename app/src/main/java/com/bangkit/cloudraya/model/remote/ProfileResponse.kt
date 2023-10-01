package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Currency(

	@field:SerializedName("symbol")
	val symbol: String,

	@field:SerializedName("initial")
	val initial: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class Data(

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("firstname")
	val firstname: String,

	@field:SerializedName("status_words")
	val statusWords: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("address1")
	val address1: String,

	@field:SerializedName("timezone")
	val timezone: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("lastname")
	val lastname: String,

	@field:SerializedName("enable_vpc")
	val enableVpc: Int,

	@field:SerializedName("country_code")
	val countryCode: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("mobile_phone")
	val mobilePhone: String,

	@field:SerializedName("company")
	val company: String,

	@field:SerializedName("currency")
	val currency: Currency,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("state")
	val state: String,

	@field:SerializedName("billingtype")
	val billingtype: String,

	@field:SerializedName("postal_code")
	val postalCode: String,

	@field:SerializedName("is_use_secprof")
	val isUseSecprof: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("status")
	val status: Int,

	@field:SerializedName("phone_code")
	val phoneCode: String
)
