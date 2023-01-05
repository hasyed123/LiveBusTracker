package com.example.bramptonbuslivetracker.network

sealed class ApiResponse<T>(val data: T? = null, val message: String? = null){
    class Success<T>(data: T): ApiResponse<T>(data)
    class Error<T>(data: T? = null, message: String? = null): ApiResponse<T>(data, message)
}
