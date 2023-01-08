package com.example.bramptonbuslivetracker.network.vehicleposition

import android.content.Context
import com.example.bramptonbuslivetracker.network.ApiResponse
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import com.example.bramptonbuslivetracker.network.vehicleposition.model.VehiclePosition
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehiclePositionRepository @Inject constructor(
    private val api: VehiclePositionApi,
    @ApplicationContext private val context: Context
    ) {
    private var vehiclePosition: VehiclePosition? = null

    private lateinit var tripDataList: List<TripData>

    init {
        loadTripData()
        GlobalScope.launch {
            while(true) {
                when (val response = getVehiclePositions()) {
                    is ApiResponse.Success -> {
                        response.data?.let {
                            vehiclePosition = it
                        }
                    }
                    else -> {}
                }
                delay(5000)
            }
        }
    }

    private suspend fun getVehiclePositions(): ApiResponse<VehiclePosition> {
        return try {
            val vehiclePosition = api.getVehiclePositions()
            ApiResponse.Success(data = vehiclePosition)
        } catch (exception: Exception) {
            ApiResponse.Error(message = "An error occurred ${exception.message.toString()}")
        }
    }

    fun getAll(): VehiclePosition? {
        return vehiclePosition
    }

    fun getTrip(tripId: String): Entity? {
        vehiclePosition?.entity?.let {
            for(entity in it) {
                if(entity.vehicle.trip.trip_id == tripId)
                    return entity
            }
        }
        return null
    }

    fun getRouteVehiclePositions(routeNum: String): List<Entity>? {
        return vehiclePosition?.entity?.filter {
            listOf(routeNum).contains(it.vehicle.trip.route_id.split('-')[0])
        }
    }

    fun getRouteList(): List<String> {
        return tripDataList.map { it.route_id }.distinct()
    }

    private fun loadTripData() {
        val list = mutableListOf<TripData>()
        val inputStream = context.assets.open("trips/trips.txt")
        inputStream.bufferedReader().forEachLine {
            val splitLine = it.split(',')
            val tripData = TripData(
                splitLine[0].split('-')[0],
                splitLine[1],
                splitLine[2],
                splitLine[3],
                splitLine[4],
                splitLine[5],
                splitLine[6],
                splitLine[7],
                splitLine[8]
            )
            list.add(tripData)
        }

        tripDataList = list
    }
}