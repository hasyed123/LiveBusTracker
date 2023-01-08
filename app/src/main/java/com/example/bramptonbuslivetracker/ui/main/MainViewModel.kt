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

    private val _routeList = MutableLiveData<List<String>>()
    val routeList: LiveData<List<String>> = _routeList

    init {
        _routeList.value = repository.getRouteList()
    }
}