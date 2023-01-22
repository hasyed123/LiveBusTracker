package com.example.bramptonbuslivetracker.ui.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Direction
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Entity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text
import com.example.bramptonbuslivetracker.R
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(intent.getStringExtra("routeNumber") ?: "")
        setContent {
            val busList = viewModel.busList.observeAsState()
            val currentDirectionId = viewModel.directionId.observeAsState()
            val directionPair = viewModel.getDirectionPair()

            Column {
                DirectionCard(directionPair = directionPair, onDirectionClick = viewModel::setDirection, currentDirectionId = currentDirectionId.value ?: 0)
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
                        cameraPositionState = cameraPosition,
                        properties = MapProperties(mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                            LocalContext.current, R.raw.style_json))
                    ) {
                        for(bus in it) {
                            Marker(
                                state = MarkerState(position = LatLng(bus.vehicle.position.latitude, bus.vehicle.position.longitude)),
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
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
fun DirectionCard(directionPair: Direction, onDirectionClick: (directionId: Int) -> Unit, currentDirectionId: Int) {
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
    else Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        DirectionButton(directionName = d1, 0, onDirectionClick, currentDirectionId, modifier = Modifier.weight(1f))
        DirectionButton(directionName = d2, 1, onDirectionClick, currentDirectionId, modifier = Modifier.weight(1f))
    }
}

@Composable
fun DirectionButton(directionName: String, directionId: Int, onDirectionClick: (directionId: Int) -> Unit, currentDirectionId: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = if (currentDirectionId == directionId) Color.Red else Color.White)
            .clickable { onDirectionClick(directionId) }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(directionName, color = if(currentDirectionId == directionId) Color.White else Color.Red, fontSize = 20.sp)
    }
}

@Preview
@Composable
fun PreviewDirectionCard() {
    DirectionCard(directionPair = Direction.NORTH_SOUTH, {}, 0)
}
