package com.bangkit.cloudraya.network

import com.bangkit.cloudraya.model.remote.*
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/v1/api/gateway/user/auth")
    suspend fun getToken(
        @Header("Content-Type") contentType: String,
        @Body body: JsonObject
    ): Response<TokenResponse>

    @GET("/v1/api/gateway/user/virtualmachines")
    suspend fun getVMList(
        @Header("Authorization") token: String
    ): Response<VMListResponse>

    @GET("/v1/api/gateway/user/virtualmachines/{id}")
    suspend fun getVMDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<VMDetailResponse>

    @POST("/v1/api/gateway/user/virtualmachines/action")
    suspend fun vmAction(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") token: String,
        @Body requestBody: JsonObject
    ): Response<VMActionResponse>

    @POST("/convert")
    suspend fun getDataGraph(
        @Header("Content-Type") contentType: String,
        @Body requestBody: JsonObject
    ): Response<DataGraphResponse>

    @POST("/convertAnomaly")
    suspend fun getDataAnomaly(
        @Header("Content-Type") contentType: String,
        @Body requestBody: JsonObject
    ): Response<DataAnomalyResponse>

    @POST("/insertDataUser")
    suspend fun insertToDatabase(
        @Header("Content-Type") contentType: String,
        @Body requestBody: JsonObject
    ): Response<InsertResponse>

    @GET("/snapshot/v1/api/gateway/user/backup")
    suspend fun getBackupList(
        @Header("Authorization") token : String,
    ) : Response<BackupListResponse>

    @HTTP(method = "DELETE", path = "/snapshot/v1/api/gateway/user/backup/{id}", hasBody = true)
    suspend fun deleteBackup(
        @Header("Authorization") token : String,
        @Path("id") id: Int,
        @Body requestBody: JsonObject
    ) : Response<ActionBackupResponse>

    @POST("/snapshot/v1/api/gateway/user/backup/restore/{id}")
    suspend fun restoreBackup(
        @Header("Authorization") token : String,
        @Path("id") id: Int,
        @Body requestBody: JsonObject
    ) : Response<ActionBackupResponse>

    @GET("/snapshot/v1/api/gateway/user/backup/config/{id}")
    suspend fun getBackupConfig(
        @Header("Authorization") token : String,
        @Path("id") id: Int
    ) : Response<BackupConfigResponse>

    @POST("/snapshot/v1/api/gateway/user/backup/config")
    suspend fun saveBackupConfig(
        @Header("Authorization") token : String,
        @Body requestBody: JsonObject
    ) : Response<BackupListResponse>

    @GET("v1/api/gateway/user/ip/public")
    suspend fun getIpPublic(
        @Header("Authorization") token : String,
    ): Response<IpPublicResponse>

    @POST("/v1/api/gateway/user/ip/private")
    suspend fun getIpPrivate(
        @Header("Authorization") token : String,
        @Header("Content-Type") content : String,
        @Body requestBody: JsonObject
    ) : Response<IpPrivateResponse>

    @POST("/v1/api/gateway/user/ip/public/detach")
    suspend fun detachPublicIP(
        @Header("Authorization") token : String,
        @Header("Content-Type") content : String,
        @Body requestBody: JsonObject
    ) : Response<IpBasicResponse>

    @POST("/v1/api/gateway/user/ip/public/attach")
    suspend fun attachPublicIp(
        @Header("Authorization") token : String,
        @Header("Content-Type") content : String,
        @Body requestBody: JsonObject
    ) : Response<IpBasicResponse>

    @POST("/v1/api/gateway/user/ip/private/acquire")
    suspend fun acquireIpPrivate(
        @Header("Authorization") token : String,
        @Header("Content-Type") content : String,
        @Body requestBody: JsonObject
    ) : Response<IpBasicResponse>

    @POST("/v1/api/gateway/user/ip/private/release")
    suspend fun releaseIpPrivate(
        @Header("Authorization") token : String,
        @Header("Content-Type") content : String,
        @Body requestBody: JsonObject
    ): Response<IpBasicResponse>

    @POST("/v1/api/gateway/user/ip/public/acquire")
    suspend fun acquireIpPublic(
        @Header("Authorization") token : String,
        @Header("Content-Type") content : String,
        @Body requestBody: JsonObject
    ) : Response<IpBasicResponse>
}