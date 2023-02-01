package com.example.bramptonbuslivetracker.data.remote.dto

import com.example.bramptonbuslivetracker.domain.model.Bus
import com.example.bramptonbuslivetracker.domain.model.VehiclePosition

data class VehiclePositionDto(
    val entity: List<Entity>,
    val header: Header
)

fun VehiclePositionDto.toVehiclePosition(): VehiclePosition {
    return VehiclePosition(busList = entity.map { element ->
        val vehicle = element.vehicle
        Bus(vehicle.trip.route_id, vehicle.trip.trip_id, vehicle.position.latitude, vehicle.position.longitude)
    })
}