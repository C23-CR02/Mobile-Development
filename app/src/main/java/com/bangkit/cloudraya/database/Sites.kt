package com.bangkit.cloudraya.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "sites")
data class Sites(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("site_name")
    val site_name: String,

    @field:SerializedName("site_url")
    val site_url: String,

    )