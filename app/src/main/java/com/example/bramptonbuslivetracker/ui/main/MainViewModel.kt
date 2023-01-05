package com.example.bramptonbuslivetracker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bramptonbuslivetracker.network.vehicleposition.VehiclePositionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: VehiclePositionRepository): ViewModel() {

    private val _currentBuses = MutableLiveData<List<String>>()
    val currentBuses: LiveData<List<String>> = _currentBuses

    fun updateData() {
        viewModelScope.launch {
            while(true) {
                val data = repository.getAll()
                data?.let {
                    val listOfTrips = mutableListOf<String>()
                    for(entity in it.entity) {
                        listOfTrips.add(entity.vehicle.trip.trip_id)
                        listOfTrips.sort()
                    }
                    _currentBuses.value = listOfTrips
                }
                delay(1000)
            }
        }
    }
}