package com.bangkit.cloudraya.model.remote

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class VMListResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: VMListData,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class ProjectTag(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: Int
) : Parcelable

@Parcelize
data class VMListData(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("recordsFiltered")
	val recordsFiltered: Int,

	@field:SerializedName("servers")
	val servers: List<ServersItem>,

	@field:SerializedName("total_page")
	val totalPage: Int,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("recordsTotal")
	val recordsTotal: Int
) : Parcelable

@Parcelize
data class ServersItem(

	@field:SerializedName("snapshot_name")
	val snapshotName: String,

	@field:SerializedName("snapshot_vm")
	val snapshotVm: String,

	@field:SerializedName("public_ip")
	val publicIp: String,

	@field:SerializedName("schedule_type")
	val scheduleType: String,

	@field:SerializedName("local_id")
	val localId: String,

	@field:SerializedName("countrycode")
	val countrycode: String,

	@field:SerializedName("template_label")
	val templateLabel: String,

	@field:SerializedName("server_id")
	val serverId: String,

	@field:SerializedName("project_tag_name")
	val projectTagName: String,

	@field:SerializedName("location_id")
	val locationId: Int,

	@field:SerializedName("network_info")
	val networkInfo: NetworkInfo,

	@field:SerializedName("project_tag")
	val projectTag: ProjectTag,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("template_type")
	val templateType: String,

	@field:SerializedName("project_tag_id")
	val projectTagId: Int,

	@field:SerializedName("state")
	val state: String,

	@field:SerializedName("launch_date")
	val launchDate: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class NetworkInfo(

	@field:SerializedName("vpc_id")
	val vpcId: Int,

	@field:SerializedName("subnet_name")
	val subnetName: String,

	@field:SerializedName("private_ip")
	val privateIp: String
) : Parcelable
