package com.example.bramptonbuslivetracker.network.vehicleposition.model

data class Trip(
    val route_id: String,
    val schedule_relationship: Int,
    val start_date: String,
    val start_time: String,
    val trip_id: String
)