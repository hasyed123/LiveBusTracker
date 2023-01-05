package com.example.bramptonbuslivetracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bramptonbuslivetracker.network.vehicleposition.VehiclePositionRepository
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: VehiclePositionRepository
    ): ViewModel() {

    private val _entity = MutableLiveData<Entity>()
    val entity: LiveData<Entity> = _entity

    private var tripId = ""

    fun init(id: String) {
        tripId = id
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            while(true) {
                _entity.value = repository.getTrip(tripId)
                delay(1000)
            }
        }
    }
}