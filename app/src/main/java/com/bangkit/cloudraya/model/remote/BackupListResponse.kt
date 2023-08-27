package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class BackupListResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("snapshots")
	val snapshots: List<SnapshotsItem>,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("total_page")
	val totalPage: Int,

	@field:SerializedName("page")
	val page: Int
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

data class ActionBackupResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: SnapshotsItem,

	@field:SerializedName("message")
	val message: String
)

data class BackupConfigResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: BackupConfig,

	@field:SerializedName("message")
	val message: String
)

data class BackupConfig(
	@field:SerializedName("config_id")
	val configId: Int,

	@field:SerializedName("days")
	val days: Int,

	@field:SerializedName("retentions")
	val retentions: Int,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("user_email")
	val userEmail: String,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("updated")
	val updated: String
)
