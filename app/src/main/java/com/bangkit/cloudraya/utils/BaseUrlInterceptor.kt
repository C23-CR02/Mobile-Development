package com.bangkit.cloudraya.utils

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor(private var baseUrl: String) : Interceptor {
    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedUrl: HttpUrl = originalRequest.url.newBuilder()
            .scheme("https")
            .host(baseUrl.toHttpUrlOrNull()?.host ?: throw IllegalArgumentException("Invalid base URL"))
            .build()

        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .build()

        return chain.proceed(modifiedRequest)
    }
}