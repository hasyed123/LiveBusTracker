package com.example.bramptonbuslivetracker.domain.use_case

import com.example.bramptonbuslivetracker.domain.repository.VehiclePositionRepository
import com.example.bramptonbuslivetracker.data.repository.model.RouteData
import javax.inject.Inject

class GetAllRoutesUseCase @Inject constructor(
    private val repository: VehiclePositionRepository
) {
    private val routeDataList = repository.getRouteDateList()

    fun getAllRoutes(): List<RouteData> {
        return routeDataList
    }
}