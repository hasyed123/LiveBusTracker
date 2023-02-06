package com.example.bramptonbuslivetracker.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.domain.model.DirectionPair
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import com.example.bramptonbuslivetracker.R
import com.example.bramptonbuslivetracker.domain.model.Bus
import com.example.bramptonbuslivetracker.shared.composable.TopBar
import com.example.bramptonbuslivetracker.shared.theme.regularSignikanegativeRed20
import com.example.bramptonbuslivetracker.shared.theme.regularSignikanegativeWhite20
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(intent.getStringExtra("routeNumber") ?: "")
        setContent {
            DetailScreen()
        }
    }
}

@Composable
fun DetailScreen() {
    val viewModel: DetailViewModel = viewModel()
    val busList = viewModel.busList.observeAsState()
    val currentDirectionId = viewModel.directionId.observeAsState()
    val directionPair = viewModel.getDirectionPair()

    Scaffold(
        topBar = {
            TopBar(
                title = "Very long Route name",
                backButton = true
            )
        }
    ) {
        Column {
            DirectionCard(
                directionPair = directionPair,
                onDirectionClick = viewModel::setDirection,
                currentDirectionId = currentDirectionId.value ?: 0
            )
            LocationCard(busList = busList.value)
        }
        Modifier.padding(it)
    }
}


@Composable
fun LocationCard(busList: List<Bus>?) {
    busList?.let {
        if(it.isNotEmpty()){
            Column {
                val location = LatLng(it[0].latitude, it[0].longitude)
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
                                state = MarkerState(position = LatLng(bus.latitude, bus.longitude)),
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
fun DirectionCard(
    directionPair: DirectionPair,
    onDirectionClick: (directionId: Int) -> Unit,
    currentDirectionId: Int
) {
    var d1 = ""
    var d2 = ""
    if(directionPair == DirectionPair.NORTH_SOUTH) {
        d1 = "Northbound"
        d2 = "Southbound"
    }
    else if(directionPair == DirectionPair.EAST_WEST) {
        d1 = "Eastbound"
        d2 = "Westbound"
    }

    if(directionPair == DirectionPair.LOOP) {
        Text("Loop")
    }
    else Row (
        modifier = Modifier.fillMaxWidth()
    ) {
        DirectionButton(
            directionName = d1,
            directionId = 0,
            onDirectionClick = onDirectionClick,
            currentDirectionId = currentDirectionId,
            modifier = Modifier.weight(1f)
        )
        DirectionButton(
            directionName = d2,
            directionId = 1,
            onDirectionClick = onDirectionClick,
            currentDirectionId = currentDirectionId,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DirectionButton(
    directionName: String,
    directionId: Int,
    onDirectionClick: (directionId: Int) -> Unit,
    currentDirectionId: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (currentDirectionId == directionId)
                    Color.Red else
                    Color.White
            )
            .clickable { onDirectionClick(directionId) }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = directionName,
            style = if(currentDirectionId == directionId)
                regularSignikanegativeWhite20.body1 else
                    regularSignikanegativeRed20.body1
        )
    }
}

@Preview
@Composable
fun PreviewDirectionCard() {
    DirectionCard(directionPair = DirectionPair.NORTH_SOUTH, {}, 0)
}

@Preview
@Composable
fun PreviewDetailTopBar() {
    TopBar(title = "Bovaird", backButton = true)
}
