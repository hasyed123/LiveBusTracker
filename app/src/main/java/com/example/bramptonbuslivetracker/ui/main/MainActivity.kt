package com.example.bramptonbuslivetracker.ui.main

import android.content.Intent
import android.inputmethodservice.Keyboard
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bramptonbuslivetracker.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateData()
        setContent {
            listOfCurrentBuses()
        }
    }

    fun startDetailActivity(tripId: String) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra("id", tripId)
            startActivity(it)
        }
    }

    @Composable
    fun listOfCurrentBuses() {
        val viewModel: MainViewModel = viewModel()
        val currentTrips = viewModel.currentBuses.observeAsState()
        currentTrips.value?.let {
            LazyColumn {
                items(it) { trip ->
                    tripCard(trip)
                }
            }
        }
    }

    @Composable
    fun tripCard(tripId: String) {
        Text(
            text = tripId,
            modifier = Modifier.clickable {
                startDetailActivity(tripId)
            }
        )
    }
}


