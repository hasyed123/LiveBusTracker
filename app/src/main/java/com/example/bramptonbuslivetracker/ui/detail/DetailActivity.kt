package com.example.bramptonbuslivetracker.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Direction
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(intent.getStringExtra("routeNumber") ?: "")
        setContent {
            val busList = viewModel.busList.observeAsState()
            val directionPair = viewModel.getDirectionPair()

            Column {
                DirectionCard(directionPair = directionPair)
                LocationCard(busList = busList.value)
            }
        }
    }
}

@Composable
fun LocationCard(busList: List<Entity>?) {
    busList?.let {
        if(it.isNotEmpty()){
            Column {
                val location = LatLng(it[0].vehicle.position.latitude, it[0].vehicle.position.longitude)
                val cameraPosition = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(location, 15f)
                }

                Box {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPosition
                    ) {
                        for(bus in it) {
                            Marker(
                                state = MarkerState(position = LatLng(bus.vehicle.position.latitude, bus.vehicle.position.longitude))
                            )
                        }
                    }
//                    Button(onClick = {
//                        cameraPosition.move(CameraUpdateFactory.newLatLng(location))
//                        cameraPosition.move(CameraUpdateFactory.zoomTo(15f))
//                    }) {
//                        Text("Center")
//                    }
                }
            }
        }
    }

}

@Composable
fun DirectionCard(directionPair: Direction) {
    var d1 = ""
    var d2 = ""
    if(directionPair == Direction.NORTH_SOUTH) {
        d1 = "Northbound"
        d2 = "Southbound"
    }
    else if(directionPair == Direction.EAST_WEST) {
        d1 = "Eastbound"
        d2 = "Westbound"
    }

    if(directionPair == Direction.LOOP) {
        Text("Loop")
    }
    else Row {
        DirectionButton(directionName = d1, 0)
        DirectionButton(directionName = d2, 1)
    }
}

@Composable
fun DirectionButton(directionName: String, directionId: Int) {
    val viewModel: DetailViewModel = viewModel()
    Button(onClick = { viewModel.setDirection(directionId) }) {
        Text(directionName)
    }
}