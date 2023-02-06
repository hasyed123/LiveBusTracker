package com.example.bramptonbuslivetracker.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bramptonbuslivetracker.domain.use_case.GetAllRoutesUseCase
import com.example.bramptonbuslivetracker.data.repository.model.RouteData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(getAllRoutesUseCase: GetAllRoutesUseCase): ViewModel() {

    private val _routeList = MutableLiveData<List<RouteData>>()
    val routeList: LiveData<List<RouteData>> = _routeList

    init {
        _routeList.value = getAllRoutesUseCase.getAllRoutes()
    }

}