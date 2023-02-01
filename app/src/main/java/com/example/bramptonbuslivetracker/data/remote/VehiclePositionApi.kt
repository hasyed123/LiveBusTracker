package com.example.bramptonbuslivetracker.data.remote

import com.example.bramptonbuslivetracker.data.remote.dto.VehiclePositionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VehiclePositionApi {
    @GET("API/VehiclePositions")
    suspend fun getVehiclePositions(
        @Query("format")
        format: String = "json"
    ): VehiclePositionDto
}