package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class VPCListResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String
)

data class DataItem(

	@field:SerializedName("update_progress")
	val updateProgress: Int,

	@field:SerializedName("vpc_cs_id")
	val vpcCsId: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("attempt_auto_delete")
	val attemptAutoDelete: Int,

	@field:SerializedName("user_vpc_networks")
	val userVpcNetworks: List<UserVpcNetworksItem>,

	@field:SerializedName("number_send_email_notif")
	val numberSendEmailNotif: Int,

	@field:SerializedName("auto_deleted_at")
	val autoDeletedAt: Any,

	@field:SerializedName("jobid")
	val jobid: String,

	@field:SerializedName("in_progress")
	val inProgress: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("left_subnet")
	val leftSubnet: Int,

	@field:SerializedName("network_address")
	val networkAddress: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("in_progress_msg")
	val inProgressMsg: Any,

	@field:SerializedName("cloudstack_id")
	val cloudstackId: Int,

	@field:SerializedName("status")
	val status: Int
)

data class UserVpcNetworksItem(

	@field:SerializedName("network_cs_id")
	val networkCsId: String,

	@field:SerializedName("update_progress")
	val updateProgress: Int,

	@field:SerializedName("user_vpc_id")
	val userVpcId: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("attempt_auto_delete")
	val attemptAutoDelete: Int,

	@field:SerializedName("jobid")
	val jobid: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("netmask")
	val netmask: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("network_acl_id")
	val networkAclId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("start_ip")
	val startIp: String,

	@field:SerializedName("network_address")
	val networkAddress: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("end_ip")
	val endIp: String,

	@field:SerializedName("gateway")
	val gateway: String,

	@field:SerializedName("cloudstack_id")
	val cloudstackId: Int,

	@field:SerializedName("status")
	val status: Int
)
