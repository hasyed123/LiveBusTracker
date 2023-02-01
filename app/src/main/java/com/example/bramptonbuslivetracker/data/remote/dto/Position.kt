package com.example.bramptonbuslivetracker.data.remote.dto

data class Position(
    val bearing: Int,
    val latitude: Double,
    val longitude: Double,
    val odometer: Int,
    val speed: Double
)