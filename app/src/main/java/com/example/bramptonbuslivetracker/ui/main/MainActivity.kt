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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
            ListOfCurrentBuses()
        }
    }

    private fun startDetailActivity(routeNumber: String) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra("routeNumber", routeNumber)
            startActivity(it)
        }
    }

    @Composable
    fun ListOfCurrentBuses() {
        val viewModel: MainViewModel = viewModel()
        val routeList = viewModel.routeList.observeAsState()
        routeList.value?.let {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.background(color = Color(139, 0, 0))
            ) {
                itemsIndexed(it.map{ it.toInt() }.sorted()) { index, routeNumber ->
                    RouteCard(routeNumber.toString())
                }
            }
        }
    }

    @Composable
    fun RouteCard(routeNumber: String) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color.LightGray)
                .clickable {
                    startDetailActivity(routeNumber)
                }
        ) {
            Text(
                text = routeNumber,
                fontSize = 30.sp,
                color = Color.Blue
            )
        }
    }

    @Preview
    @Composable
    fun previewRouteCard() {
        RouteCard("34")
    }

}


