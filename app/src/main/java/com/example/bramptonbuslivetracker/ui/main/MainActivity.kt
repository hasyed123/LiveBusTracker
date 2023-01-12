package com.example.bramptonbuslivetracker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    fun startDetailActivity(routeNumber: String) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra("routeNumber", routeNumber)
            startActivity(it)
        }
    }

    @Composable
    fun listOfCurrentBuses() {
        val viewModel: MainViewModel = viewModel()
        val routeList = viewModel.routeList.observeAsState()
        routeList.value?.let {
            LazyColumn {
                items(it.map{ it.toInt() }.sorted()) { routeNumber ->
                    routeCard(routeNumber.toString())
                }
            }
        }
    }

    @Composable
    fun routeCard(routeNumber: String) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
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
        routeCard("34")
    }
}


