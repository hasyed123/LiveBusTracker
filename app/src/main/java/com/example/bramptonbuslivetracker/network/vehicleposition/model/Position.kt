package com.example.bramptonbuslivetracker.network.vehicleposition.model

data class Position(
    val bearing: Int,
    val latitude: Double,
    val longitude: Double,
    val odometer: Int,
    val speed: Double
)