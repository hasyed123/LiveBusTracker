package com.example.bramptonbuslivetracker.data.remote.dto

data class Entity(
    val alert: Any,
    val id: String,
    val is_deleted: Boolean,
    val trip_update: Any,
    val vehicle: Vehicle
)