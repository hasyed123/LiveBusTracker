package com.example.bramptonbuslivetracker.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.domain.model.DirectionPair
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import com.example.bramptonbuslivetracker.R
import com.example.bramptonbuslivetracker.domain.model.Bus
import com.example.bramptonbuslivetracker.shared.composable.TopBar
import com.example.bramptonbuslivetracker.shared.theme.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(
            routeNumber = intent.getStringExtra("routeNumber") ?: "",
            routeName = intent.getStringExtra("routeName") ?: ""
        )
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
    val directionSwitched = viewModel.directionSwitched
    val directionPair = viewModel.getDirectionPair()

    Scaffold(
        topBar = {
            TopBar(
                title = viewModel.getRouteName(),
                backButton = true
            )
        }
    ) {
        LocationCard(
            busList = busList.value,
            directionPair = directionPair,
            onDirectionClick = viewModel::setDirection,
            currentDirectionId = currentDirectionId.value,
            directionSwitched = directionSwitched,
            saveDirection = viewModel::saveDirection
        )
        Modifier.padding(it)
    }
}


@Composable
fun LocationCard(
    busList: List<Bus>?,
    directionPair: DirectionPair,
    onDirectionClick: (directionId: Int) -> Unit,
    currentDirectionId: Int?,
    directionSwitched: LiveData<Boolean>,
    saveDirection: () -> Unit
) {
    Box {
        busList?.let {
            if(it.isNotEmpty()){
                Column {
                    val location = LatLng(it[0].latitude, it[0].longitude)
                    val cameraPosition = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(location, 11f)
                    }
                    if(directionSwitched.value == true) {
                        val newLocation = LatLng(it[0].latitude, it[0].longitude)
                        cameraPosition.move(CameraUpdateFactory.newLatLng(newLocation))
                        saveDirection()
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
                    }
                }
            }
            else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Tracking information not available for selected route/direction",
                        style = regularSignikanegativeBlue30.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        DirectionCard(
            directionPair = directionPair,
            onDirectionClick = onDirectionClick,
            currentDirectionId = currentDirectionId ?: 0
        )
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

    if(directionPair != DirectionPair.LOOP) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            DirectionButton(
                directionName = d1,
                directionId = 0,
                onDirectionClick = onDirectionClick,
                currentDirectionId = currentDirectionId
            )
            DirectionButton(
                directionName = d2,
                directionId = 1,
                onDirectionClick = onDirectionClick,
                currentDirectionId = currentDirectionId
            )
        }
    }
}

@Composable
fun DirectionButton(
    directionName: String,
    directionId: Int,
    onDirectionClick: (directionId: Int) -> Unit,
    currentDirectionId: Int
) {
    Button(
        onClick = { onDirectionClick(directionId) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (currentDirectionId == directionId)
                regularBlue else
                transparentWhite
        ),
        shape = RoundedCornerShape(80.dp),
        modifier = Modifier
            .padding(4.dp),
        elevation = ButtonDefaults.elevation(100.dp)
    ) {
        Text(
            text = directionName,
            style = if(currentDirectionId == directionId)
                regularSignikanegativeWhite20.body1 else
                    regularSignikanegativeBlue20.body1
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
    TopBar(title = "Bovaird", backButton = false)
}
