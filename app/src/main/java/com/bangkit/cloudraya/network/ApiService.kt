package com.bangkit.cloudraya.network

import com.bangkit.cloudraya.model.remote.TokenResponse
import com.bangkit.cloudraya.model.remote.VMListResponse
import com.bangkit.cloudraya.model.remote.VMActionResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("/v1/api/gateway/user/auth")
    suspend fun getToken(
        @Header("Content-Type") contentType : String,
        @Body body : JsonObject
    ): Response<TokenResponse>

    @GET("/v1/api/gateway/gateway/user/virtualmachines")
    suspend fun getVMList(
        @Header("Authorization") token : String
    ):Response<VMListResponse>

    @POST("/v1/api/gateway/gateway/user/virtualmachines/action")
    suspend fun vmAction(
        @Header("Content-Type") contentType : String,
        @Header("Authorization") token : String,
    ) : Response<VMActionResponse>
}