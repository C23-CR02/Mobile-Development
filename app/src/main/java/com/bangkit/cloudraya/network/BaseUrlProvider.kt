package com.bangkit.cloudraya.network

class BaseUrlProvider(private var baseUrl: String) {
    fun getBaseUrl(): String = baseUrl

    fun setBaseUrl(url: String) {
        baseUrl = url
    }
}