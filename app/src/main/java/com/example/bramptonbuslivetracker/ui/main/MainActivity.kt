package com.example.bramptonbuslivetracker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.network.vehicleposition.model.Direction
import com.example.bramptonbuslivetracker.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            listOfCurrentBuses()
        }
    }

    fun startDetailActivity(routeNumber: String, directionId: Int) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra("routeNumber", routeNumber)
            it.putExtra("directionId", directionId)
            startActivity(it)
        }
    }

    var mLastClickTime = 0L
    fun isClickRecently(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 200) {
            return true
        }
        return false
    }

    @Composable
    fun listOfCurrentBuses() {
        val viewModel: MainViewModel = viewModel()
        val routeList = viewModel.routeList.observeAsState()
        routeList.value?.let {
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 100.dp)) {
                itemsIndexed(it.map{ it.toInt() }.sorted()) { index, routeNumber ->
                    val color = if(index%2==0) Color.LightGray
                    else Color.Green
                    routeCard(routeNumber.toString(), color)
                }
            }
        }
    }

    @Composable
    fun routeCard(routeNumber: String, color: Color) {
        var popupControl by remember { mutableStateOf(false) }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = color)
                .clickable {
                    if(!isClickRecently()) {
                        popupControl = popupControl==false
                    }
                    //startDetailActivity(routeNumber)
                }
        ) {
            Text(
                text = routeNumber,
                fontSize = 30.sp,
                color = Color.Blue
            )

            if (popupControl) {
                var d1 = "North"
                var d2 = "South"
                when(viewModel.getDirection(routeNumber)) {
                    Direction.LOOP -> {
                        startDetailActivity(routeNumber, 2)
                        popupControl = false
                    }
                    Direction.EAST_WEST -> {
                        d1 = "East"
                        d2 = "West"
                    }
                    else -> {}
                }
                if(popupControl) {
                    Popup(onDismissRequest = {
                        popupControl = false
                        mLastClickTime = SystemClock.elapsedRealtime()
                    }) {
                        Column {
                            Text(
                                text = d1,
                                modifier = Modifier.clickable {
                                    startDetailActivity(routeNumber, 0)
                                }
                            )
                            Text(
                                text = d2,
                                modifier = Modifier.clickable {
                                    startDetailActivity(routeNumber, 1)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun previewRouteCard() {
        routeCard("34", Color.White)
    }

    @Composable
    fun directionPopup(direction: Direction) {

    }
}


