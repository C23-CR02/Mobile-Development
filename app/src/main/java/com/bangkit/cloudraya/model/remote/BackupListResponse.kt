package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class BackupListResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataBackUp? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SnapshotsItem(

	@field:SerializedName("backup_id")
	val backupId: Int,

	@field:SerializedName("hostname")
	val hostname: String,

	@field:SerializedName("location_name")
	val locationName: String,

	@field:SerializedName("user_email")
	val userEmail: String,

	@field:SerializedName("backup_name")
	val backupName: String,

	@field:SerializedName("size")
	val size: String,

	@field:SerializedName("location_code")
	val locationCode: String,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("format")
	val format: String,

	@field:SerializedName("updated")
	val updated: String,

	@field:SerializedName("status")
	val status: String
)

data class DataBackUp(

	@field:SerializedName("snapshots")
	val snapshots: List<SnapshotsItem>,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("total_page")
	val totalPage: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null
)

data class ActionBackupResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: ActionData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ActionData(

	@field:SerializedName("user_email")
	val userEmail: String? = null,

	@field:SerializedName("config_id")
	val configId: Int? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("days")
	val days: Int? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("retentions")
	val retentions: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)


data class BackupConfigResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: BackupConfigData ,

	@field:SerializedName("message")
	val message: String? = null
)

data class BackupConfigData(

	@field:SerializedName("user_email")
	val userEmail: String ,

	@field:SerializedName("config_id")
	val configId: Int ,

	@field:SerializedName("created")
	val created: String  ,

	@field:SerializedName("days")
	val days: Int,

	@field:SerializedName("updated")
	val updated: String,

	@field:SerializedName("retentions")
	val retentions: Int,

	@field:SerializedName("status")
	val status: String
)
