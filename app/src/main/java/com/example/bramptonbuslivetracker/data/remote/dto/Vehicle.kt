package com.example.bramptonbuslivetracker.data.remote.dto

data class Vehicle(
    val congestion_level: Int,
    val current_status: Int,
    val current_stop_sequence: Int,
    val position: Position,
    val stop_id: String,
    val timestamp: Int,
    val trip: Trip,
    val vehicle: VehicleX
)