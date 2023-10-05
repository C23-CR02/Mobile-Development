package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class TemplateResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class TemplatesItem(

	@field:SerializedName("disk_size_required")
	val diskSizeRequired: Int? = null,

	@field:SerializedName("os")
	val os: String? = null,

	@field:SerializedName("ostypeid")
	val ostypeid: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("core_number")
	val coreNumber: Int? = null,

	@field:SerializedName("setting_min_core")
	val settingMinCore: Int? = null,

	@field:SerializedName("displaytext")
	val displaytext: String? = null,

	@field:SerializedName("enable_core")
	val enableCore: Int? = null,

	@field:SerializedName("cs_template_id")
	val csTemplateId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ostypename")
	val ostypename: String? = null,

	@field:SerializedName("size_gb")
	val sizeGb: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status_text")
	val statusText: String? = null,

	@field:SerializedName("prices")
	val prices: List<PricesItem?>? = null,

	@field:SerializedName("setting_max_core")
	val settingMaxCore: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("last_sync")
	val lastSync: LastSync? = null,

	@field:SerializedName("templates")
	val templates: List<TemplatesItem?>? = null
)

data class LastSync(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("timezone_type")
	val timezoneType: Int? = null
)

data class PricesItem(

	@field:SerializedName("price_os_monthly")
	val priceOsMonthly: Int? = null,

	@field:SerializedName("price_os")
	val priceOs: Int? = null,

	@field:SerializedName("currency")
	val currency: String? = null
)
