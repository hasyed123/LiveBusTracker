package com.example.bramptonbuslivetracker.domain.repository

import com.example.bramptonbuslivetracker.data.remote.dto.VehiclePositionDto
import com.example.bramptonbuslivetracker.data.repository.model.TripData
import com.example.bramptonbuslivetracker.data.repository.model.RouteData

interface VehiclePositionRepository {

    suspend fun getVehiclePositions(): VehiclePositionDto?

    fun getTripDataList(): List<TripData>

    fun getRouteDateList(): List<RouteData>
}