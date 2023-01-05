package com.example.bramptonbuslivetracker.network.vehicleposition.model

data class Entity(
    val alert: Any,
    val id: String,
    val is_deleted: Boolean,
    val trip_update: Any,
    val vehicle: Vehicle
)