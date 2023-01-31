package com.example.bramptonbuslivetracker.network.vehicleposition

import android.content.Context
import com.example.bramptonbuslivetracker.network.ApiResponse
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Direction
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import com.example.bramptonbuslivetracker.network.vehicleposition.model.RouteData
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

    private lateinit var routeDataList: List<RouteData>

    init {
        loadRoutes()
        GlobalScope.launch {
            loadTripData()
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

    fun getRouteVehiclePositions(routeNum: String, directionId: Int): List<Entity>? {
        val list = vehiclePosition?.entity?.filter {
            listOf(routeNum).contains(it.vehicle.trip.route_id.split('-')[0])
        }
        return if(directionId == 2) list
        else {
            val tripsInDirection = tripDataList.filter {
                listOf(directionId.toString()).contains(it.direction_id) && listOf(routeNum).contains(it.route_id)
            }.map { it.trip_id }

            list?.filter {
                tripsInDirection.contains(it.vehicle.trip.trip_id)
            }
        }
    }

    fun getRouteList(): List<RouteData> {
        return routeDataList
    }

    fun getDirection(routeNum: String): Direction {
        val routeTripData = tripDataList.filter { listOf(routeNum).contains(it.route_id) }
        val headsignList = routeTripData.map { it.trip_headsign }
        val directionList = routeTripData.map { it.direction_id }.distinct()

        return if(directionList.size==2) {
            if(headsignList.any { it.contains("north", true) } && headsignList.any { it.contains("south", true) }) {
                Direction.NORTH_SOUTH
            }
            else if(headsignList.any { it.contains("nb", true) } && headsignList.any { it.contains("sb", true) }) {
                Direction.NORTH_SOUTH
            }
            else if(headsignList.any { it.contains(" AM") } && headsignList.any { it.contains(" PM") }) {
                Direction.LOOP
            }
            else Direction.EAST_WEST
        } else Direction.LOOP

    }

    private fun loadTripData() {
        val list = mutableListOf<TripData>()
        val inputStream = context.assets.open("transit info/trips.txt")
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

    private fun loadRoutes() {
        val list = mutableListOf<RouteData>()
        val inputStream = context.assets.open("transit info/routes.txt")
        inputStream.bufferedReader().forEachLine {
            val splitLine = it.split(',')
            val routeData = RouteData(
                splitLine[1],
                splitLine[2].substring(1, splitLine[2].length-1)
            )
            list.add(routeData)
        }

        routeDataList = list
    }
}