package com.example.bramptonbuslivetracker.data.repository

import android.content.Context
import com.example.bramptonbuslivetracker.data.remote.VehiclePositionApi
import com.example.bramptonbuslivetracker.data.remote.ApiResponse
import com.example.bramptonbuslivetracker.data.remote.dto.VehiclePositionDto
import com.example.bramptonbuslivetracker.domain.repository.VehiclePositionRepository
import com.example.bramptonbuslivetracker.data.repository.model.TripData
import com.example.bramptonbuslivetracker.data.repository.model.RouteData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class VehiclePositionRepositoryImpl @Inject constructor(
    private val api: VehiclePositionApi,
    @ApplicationContext private val context: Context
    ): VehiclePositionRepository {

    private lateinit var tripDataList: List<TripData>
    private lateinit var routeDataList: List<RouteData>

    private var vehiclePositionDto: VehiclePositionDto? = null

    init {
        loadRoutes()

        GlobalScope.launch {
            loadTripData()
            while(true) {
                fetchVehiclePositions()
                delay(5000)
            }
        }
    }

    private suspend fun fetchVehiclePositions() {
        vehiclePositionDto = try {
            val vehiclePosition = api.getVehiclePositions()
            ApiResponse.Success(data = vehiclePosition)
        } catch (exception: Exception) {
            ApiResponse.Error(message = "An error occurred ${exception.message.toString()}")
        }.data
    }

    override suspend fun getVehiclePositions(): VehiclePositionDto? {
        return vehiclePositionDto
    }

    override fun getTripDataList(): List<TripData> {
        return tripDataList
    }

    override fun getRouteDateList(): List<RouteData> {
        return routeDataList
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