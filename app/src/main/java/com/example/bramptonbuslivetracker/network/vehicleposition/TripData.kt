package com.example.bramptonbuslivetracker.network.vehicleposition

data class TripData(
    val route_id: String,
    val service_id: String,
    val trip_id: String,
    val trip_headsign: String,
    val direction_id: String,
    val block_id: String,
    val shape_id: String,
    val wheelchair_accessible: String,
    val bikes_allowed: String
)