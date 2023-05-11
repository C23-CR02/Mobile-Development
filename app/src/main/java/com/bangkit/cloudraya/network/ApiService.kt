package com.bangkit.cloudraya.network

import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.model.remote.VMListResponse
import com.bangkit.cloudraya.model.remote.VMActionResponse
import com.bangkit.cloudraya.model.remote.VMDetailResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("user/auth")
    suspend fun getToken(
        @Header("Content-Type") contentType : String,
        @Body body : JsonObject
    ): Response<TokenResponse>

    @GET("user/virtualmachines")
    suspend fun getVMList(
        @Header("Authorization") token : String
    ):Response<VMListResponse>

    @GET("user/virtualmachines/{id}")
    suspend fun getVMDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<VMDetailResponse>

    @POST("user/virtualmachines/action")
    suspend fun vmAction(
        @Header("Content-Type") contentType : String,
        @Header("Authorization") token : String,
        @Body requestBody: JsonObject
    ) : Response<VMActionResponse>
}