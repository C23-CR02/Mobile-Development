package com.bangkit.cloudraya.model.remote

import com.google.gson.annotations.SerializedName

data class IpVMOwn(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: DataIps,

	@field:SerializedName("message")
	val message: String
)

data class PrivateIpsItem(

	@field:SerializedName("ipaddress")
	val ipaddress: String,

	@field:SerializedName("isdefault")
	val isdefault: Int,

	@field:SerializedName("private_ip_id")
	val id: String,

	@field:SerializedName("is_used")
	val isUsed: Int
)

data class PublicIpsItem(

	@field:SerializedName("public_ip_id")
	val publicIpId: Int,

	@field:SerializedName("private_ip_id")
	val privateIpId: String,

	@field:SerializedName("public")
	val jsonMemberPublic: String,

	@field:SerializedName("private")
	val jsonMemberPrivate: String,

	@field:SerializedName("cost")
	val cost: Int,

	@field:SerializedName("is_primary")
	val isPrimary: Int,

	@field:SerializedName("project_tag")
	val projectTag: Any,

	@field:SerializedName("project_tag_id")
	val projectTagId: Any,

	@field:SerializedName("include_public_ip")
	val includePublicIp: Int,


)

data class DataIps(

	@field:SerializedName("private_ips")
	val privateIps: List<PrivateIpsItem>,

	@field:SerializedName("public_ips")
	val publicIps: List<PublicIpsItem>
)
