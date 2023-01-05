package com.example.bramptonbuslivetracker.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import com.example.bramptonbuslivetracker.ui.main.MainViewModel
import com.google.android.gms.maps.CameraUpdate
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
        viewModel.init(intent.getStringExtra("id") ?: "")
        setContent {
            val entity = viewModel.entity.observeAsState()
            LocationCard(entity = entity.value)
        }
    }
}

@Composable
fun LocationCard(entity: Entity?) {
    entity?.let {
        Column {
            Text(it.vehicle.position.latitude.toString())
            Text(it.vehicle.position.longitude.toString())

            val location = LatLng(it.vehicle.position.latitude, it.vehicle.position.longitude)
            val cameraPosition = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(location, 15f)
            }

            Box {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPosition
                ) {
                    Marker(
                        state = MarkerState(position = location)
                    )
                }
                Button(onClick = {
                    cameraPosition.move(CameraUpdateFactory.newLatLng(location))
                    cameraPosition.move(CameraUpdateFactory.zoomTo(15f))
                }) {
                    Text("Center")
                }
            }
        }
    }
}