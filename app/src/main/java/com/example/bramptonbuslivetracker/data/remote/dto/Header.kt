package com.example.bramptonbuslivetracker.data.remote.dto

data class Header(
    val gtfs_realtime_version: String,
    val incrementality: Int,
    val timestamp: Int
)