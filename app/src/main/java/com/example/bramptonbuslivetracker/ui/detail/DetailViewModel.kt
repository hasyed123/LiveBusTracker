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

    private val _direction = MutableLiveData<Direction>()
    val direction: LiveData<Direction> = _direction

    private var routeNumber = ""
    private var directionId = 2

    fun init(routeNumber: String, directionId: Int) {
        this.routeNumber = routeNumber
        this.directionId = directionId
        _direction.value = repository.getDirection(routeNumber)
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            while(true) {
                _busList.value = repository.getRouteVehiclePositions(routeNumber, directionId)
                delay(1000)
            }
        }
    }
}