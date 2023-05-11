package com.bangkit.cloudraya.network

import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.model.remote.VMListResponse
import com.bangkit.cloudraya.model.remote.VMActionResponse
import com.bangkit.cloudraya.model.remote.VMDetailResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("user/auth")
    suspend fun getToken(
        @Header("Content-Type") contentType : String,
        @Body body : JsonObject
    ): Response<TokenResponse>

    @GET("/user/virtualmachines")
    suspend fun getVMList(
        @Header("Authorization") token : String
    ):Response<VMListResponse>

    @GET("/user/virtualmachines")
    suspend fun getVMDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): Response<VMDetailResponse>

    @POST("/user/virtualmachines/action")
    suspend fun vmAction(
        @Header("Content-Type") contentType : String,
        @Header("Authorization") token : String,
        @Body requestBody: Map<String, Any>
    ) : Response<VMActionResponse>
}