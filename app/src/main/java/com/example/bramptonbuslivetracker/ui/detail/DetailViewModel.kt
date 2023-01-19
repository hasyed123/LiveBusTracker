package com.example.bramptonbuslivetracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bramptonbuslivetracker.network.vehicleposition.VehiclePositionRepository
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Direction
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: VehiclePositionRepository
    ): ViewModel() {

    private val _busList = MutableLiveData<List<Entity>>()
    val busList: LiveData<List<Entity>> = _busList

    private val _directionId = MutableLiveData<Int>()
    val directionId: LiveData<Int> = _directionId

    private var routeNumber = ""

    fun init(routeNumber: String) {
        _directionId.value = 0
        this.routeNumber = routeNumber
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            while(true) {
                _busList.value =
                    directionId.value?.let { repository.getRouteVehiclePositions(routeNumber, it) }
                delay(1000)
            }
        }
    }

    fun setDirection(directionId: Int) {
        _directionId.value = directionId
        _busList.value = repository.getRouteVehiclePositions(routeNumber, directionId)
    }

    fun getDirectionPair(): Direction {
        repository.getDirection(routeNumber).also {
            if(it == Direction.LOOP) _directionId.value = 2
            return it
        }

    }
}