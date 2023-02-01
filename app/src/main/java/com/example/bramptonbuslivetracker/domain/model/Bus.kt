package com.example.bramptonbuslivetracker.domain.model

data class Bus(
    val route_id: String,
    val trip_id: String,
    val latitude: Double,
    val longitude: Double,
)
