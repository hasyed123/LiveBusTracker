package com.example.bramptonbuslivetracker.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Direction
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import com.google.android.gms.maps.CameraUpdateFactory
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
            LocationCard(busList = busList.value, direction = viewModel.direction.value)
        }
    }
}

@Composable
fun LocationCard(busList: List<Entity>?, direction: Direction?) {
    busList?.let {
        if(it.isNotEmpty()){
            Column {
                val text = when(direction) {
                    Direction.NORTH_SOUTH -> "ns"
                    Direction.EAST_WEST -> "ew"
                    Direction.LOOP -> "l"
                    else -> "null"
                }

                Text(text)

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

    if(busList.isNullOrEmpty()) {
        val text = when(direction) {
            Direction.NORTH_SOUTH -> "ns"
            Direction.EAST_WEST -> "ew"
            Direction.LOOP -> "l"
            else -> "null"
        }
        Text(text)
        //Text("Tracking information not available for the selected route.")
    }
}