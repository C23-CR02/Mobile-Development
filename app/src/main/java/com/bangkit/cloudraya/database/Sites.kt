package com.bangkit.cloudraya.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "sites")
data class Sites(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("site_name")
    val site_name: String,

    @field:SerializedName("site_url")
    val site_url: String
    )