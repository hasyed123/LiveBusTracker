package com.example.bramptonbuslivetracker.domain.use_case

import com.example.bramptonbuslivetracker.data.remote.dto.toVehiclePosition
import com.example.bramptonbuslivetracker.domain.model.Bus
import com.example.bramptonbuslivetracker.domain.repository.VehiclePositionRepository
import com.example.bramptonbuslivetracker.data.repository.model.TripData
import javax.inject.Inject

class GetRouteBusesUseCase @Inject constructor(
    private val repository: VehiclePositionRepository
) {
    private val tripDataList: List<TripData> = repository.getTripDataList()

    suspend fun getRouteBuses(routeNum: String, directionId: Int): List<Bus>? {
        val vehiclePosition = repository.getVehiclePositions()?.toVehiclePosition()
        val list = vehiclePosition?.busList?.filter {
            listOf(routeNum).contains(it.route_id.split('-')[0])
        }
        return if(directionId == 2) list
        else {
            val tripsInDirection = tripDataList.filter {
                listOf(directionId.toString()).contains(it.direction_id) && listOf(routeNum).contains(it.route_id)
            }.map { it.trip_id }

            list?.filter {
                tripsInDirection.contains(it.trip_id)
            }
        }
    }

}