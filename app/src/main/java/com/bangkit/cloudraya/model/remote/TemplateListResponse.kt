package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class TemplateListResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class LastSync(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("timezone")
	val timezone: String,

	@field:SerializedName("timezone_type")
	val timezoneType: Int
)

data class Data(

	@field:SerializedName("last_sync")
	val lastSync: LastSync,

	@field:SerializedName("templates")
	val templates: List<TemplatesItem>
)

data class PricesItem(

	@field:SerializedName("price_os_monthly")
	val priceOsMonthly: Int,

	@field:SerializedName("price_os")
	val priceOs: Int,

	@field:SerializedName("currency")
	val currency: String
)

data class TemplatesItem(

	@field:SerializedName("disk_size_required")
	val diskSizeRequired: Int,

	@field:SerializedName("os")
	val os: String,

	@field:SerializedName("ostypeid")
	val ostypeid: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("core_number")
	val coreNumber: Int,

	@field:SerializedName("setting_min_core")
	val settingMinCore: Int,

	@field:SerializedName("displaytext")
	val displaytext: String,

	@field:SerializedName("enable_core")
	val enableCore: Int,

	@field:SerializedName("cs_template_id")
	val csTemplateId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("ostypename")
	val ostypename: String,

	@field:SerializedName("size_gb")
	val sizeGb: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status_text")
	val statusText: String,

	@field:SerializedName("prices")
	val prices: List<PricesItem>,

	@field:SerializedName("setting_max_core")
	val settingMaxCore: Int,

	@field:SerializedName("status")
	val status: Int
)
