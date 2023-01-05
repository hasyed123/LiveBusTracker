package com.example.bramptonbuslivetracker.network.vehicleposition

import com.example.bramptonbuslivetracker.network.vehicleposition.model.VehiclePosition
import retrofit2.http.GET
import retrofit2.http.Query

interface VehiclePositionApi {
    @GET("API/VehiclePositions")
    suspend fun getVehiclePositions(
        @Query("format")
        format: String = "json"
    ): VehiclePosition
}