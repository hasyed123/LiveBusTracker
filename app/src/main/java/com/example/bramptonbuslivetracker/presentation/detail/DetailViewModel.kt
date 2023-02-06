package com.example.bramptonbuslivetracker.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bramptonbuslivetracker.domain.model.DirectionPair
import com.example.bramptonbuslivetracker.domain.model.Bus
import com.example.bramptonbuslivetracker.domain.use_case.GetRouteBusesUseCase
import com.example.bramptonbuslivetracker.domain.use_case.GetRouteDirectionPairUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getRouteBusesUseCase: GetRouteBusesUseCase,
    private val getRouteDirectionPairUseCase: GetRouteDirectionPairUseCase
    ): ViewModel() {

    private val _busList = MutableLiveData<List<Bus>>()
    val busList: LiveData<List<Bus>> = _busList

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
                    directionId.value?.let { getRouteBusesUseCase.getRouteBuses(routeNumber, it) }
                delay(1000)
            }
        }
    }

    fun setDirection(directionId: Int) {
        if(directionId != _directionId.value) {
            _directionId.value = directionId
            viewModelScope.launch {
                _busList.value = getRouteBusesUseCase.getRouteBuses(routeNumber, directionId)
            }
        }
    }

    fun getDirectionPair(): DirectionPair {
        getRouteDirectionPairUseCase.getDirection(routeNumber).also {
            if(it == DirectionPair.LOOP) _directionId.value = 2
            return it
        }
    }
}