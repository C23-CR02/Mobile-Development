package com.bangkit.cloudraya.model.local

sealed class Event<out R> private constructor() {
    data class Success<out T>(val data: T) : Event<T>()
    data class Error(val code: Int? = null, val error: String? = null) : Event<Nothing>()
    object Loading : Event<Nothing>()

    fun getContentIfNotHandled(): R? {
        return when (this) {
            is Success -> data
            else -> null
        }
    }
}