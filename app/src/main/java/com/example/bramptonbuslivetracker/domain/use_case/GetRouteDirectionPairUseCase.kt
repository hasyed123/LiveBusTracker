package com.example.bramptonbuslivetracker.domain.use_case

import com.example.bramptonbuslivetracker.domain.repository.VehiclePositionRepository
import com.example.bramptonbuslivetracker.data.repository.model.TripData
import com.example.bramptonbuslivetracker.domain.model.DirectionPair
import javax.inject.Inject

class GetRouteDirectionPairUseCase@Inject constructor(
    private val repository: VehiclePositionRepository
) {
    private val tripDataList: List<TripData> = repository.getTripDataList()

    fun getDirection(routeNum: String): DirectionPair {
        val routeTripData = tripDataList.filter { listOf(routeNum).contains(it.route_id) }
        val headsignList = routeTripData.map { it.trip_headsign }
        val directionList = routeTripData.map { it.direction_id }.distinct()

        return if(directionList.size==2) {
            if(headsignList.any { it.contains("north", true) } && headsignList.any { it.contains("south", true) }) {
                DirectionPair.NORTH_SOUTH
            }
            else if(headsignList.any { it.contains("nb", true) } && headsignList.any { it.contains("sb", true) }) {
                DirectionPair.NORTH_SOUTH
            }
            else if(headsignList.any { it.contains(" AM") } && headsignList.any { it.contains(" PM") }) {
                DirectionPair.LOOP
            }
            else DirectionPair.EAST_WEST
        } else DirectionPair.LOOP

    }
}