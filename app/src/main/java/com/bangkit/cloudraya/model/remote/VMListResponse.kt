package com.bangkit.cloudraya.model.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class VMListResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<VMListData>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class VMListData(

	@field:SerializedName("schedule_type")
	val scheduleType: String? = null,

	@field:SerializedName("public_ip")
	val publicIp: String? = null,

	@field:SerializedName("local_id")
	val localId: Int? = null,

	@field:SerializedName("countrycode")
	val countrycode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("template_label")
	val templateLabel: String? = null,

	@field:SerializedName("template_type")
	val templateType: String? = null,

	@field:SerializedName("launch_date")
	val launchDate: String? = null,

	@field:SerializedName("server_id")
	val serverId: String? = null,

	@field:SerializedName("location_id")
	val locationId: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
): Parcelable
