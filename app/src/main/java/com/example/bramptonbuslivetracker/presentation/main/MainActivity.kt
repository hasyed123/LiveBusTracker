package com.example.bramptonbuslivetracker.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.presentation.detail.DetailActivity
import com.example.bramptonbuslivetracker.shared.composable.TopBar
import com.example.bramptonbuslivetracker.shared.theme.regularBlue
import com.example.bramptonbuslivetracker.shared.theme.regularSignikanegativeBlue
import com.example.bramptonbuslivetracker.shared.theme.regularSignikanegativeBlue30
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MainScreen()
        }
    }

    private fun startDetailActivity(routeNumber: String, routeName: String) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra("routeNumber", routeNumber)
            it.putExtra("routeName", routeName)
            startActivity(it)
        }
    }

    @Composable
    fun MainScreen() {
        val viewModel: MainViewModel = viewModel()
        
        Scaffold(
            topBar = {
               TopBar(
                   title = "Routes",
                   backButton = false
               )
            }
        ) {
            ListOfCurrentBuses(viewModel)
            Modifier.padding(it)
        }
    }
    
    @Composable
    fun ListOfCurrentBuses(viewModel: MainViewModel) {
        val routeList = viewModel.routeList.observeAsState()
        routeList.value?.let {

            Column(
                Modifier.background(color = regularBlue)
            ) {
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
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    startDetailActivity(num, name)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = num,
                    style = regularSignikanegativeBlue30.body1
                )
                Text(
                    text = name,
                    textAlign = TextAlign.Center,
                    style = regularSignikanegativeBlue.body1
                )
            }
        }
    }

    @Preview
    @Composable
    fun previewRouteCard() {
        RouteCard("34", "Pearson Airport Express")
    }

}


