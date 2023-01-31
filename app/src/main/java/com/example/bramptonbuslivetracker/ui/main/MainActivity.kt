package com.example.bramptonbuslivetracker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.R
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

            Column(
                Modifier.background(color = Color(0,0,255,200))
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), contentAlignment = Alignment.Center) {
                    Text("Routes", fontSize = 20.sp,modifier = Modifier.background(color = Color.Transparent), color = Color.White, fontFamily = FontFamily(
                        Font(R.font.signikanegative_regular)
                    )
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    itemsIndexed(it.sortedBy { it.num.toInt() }) { index, routeNumber ->
                        RouteCard(routeNumber.name, routeNumber.num.toString())
                    }
                }
            }
        }
    }

    @Composable
    fun RouteCard(name: String, num: String) {
        val backgroundColor = Color.LightGray
        val secondaryColor = Color.White
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor,
                            secondaryColor
                        )
                    ), shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    startDetailActivity(num)
                }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
                Text(
                    text = num,
                    fontSize = 30.sp,
                    color = Color.Blue,
                    fontFamily = FontFamily(
                        Font(R.font.signikanegative_regular))
                )
                Text(name, color = Color.Blue, textAlign = TextAlign.Center, fontFamily = FontFamily(
                    Font(R.font.signikanegative_regular)))
            }
        }
    }

    @Preview
    @Composable
    fun previewRouteCard() {
        RouteCard("34", "Pearson Airport Express")
    }

}


