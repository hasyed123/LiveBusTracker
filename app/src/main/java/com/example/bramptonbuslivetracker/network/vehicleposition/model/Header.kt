package com.example.bramptonbuslivetracker.network.vehicleposition.model

data class Header(
    val gtfs_realtime_version: String,
    val incrementality: Int,
    val timestamp: Int
)