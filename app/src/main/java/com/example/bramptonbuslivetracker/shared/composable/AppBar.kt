package com.example.bramptonbuslivetracker.shared.composable

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bramptonbuslivetracker.shared.theme.regularBlue
import com.example.bramptonbuslivetracker.shared.theme.regularSignikanegativeWhite20

@Composable
fun TopBar(title: String, backButton: Boolean) {
    Box(
        modifier = Modifier
            .background(color = regularBlue)
            .fillMaxWidth()
            .padding(
                bottom = 12.dp
            )
    ) {
        if(backButton) {
            val activity = LocalContext.current as Activity
            IconButton(onClick = { activity.onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.align(Alignment.CenterStart),
                    tint = Color.White
                )
            }
        }
        Text(
            text = title,
            style = regularSignikanegativeWhite20.body1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}