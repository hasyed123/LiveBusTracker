package com.example.bramptonbuslivetracker.shared.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.bramptonbuslivetracker.R

val regularSignikanegativeBlue = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.signikanegative_regular)),
        color = regularBlue
    )
)

val regularSignikanegativeBlue20 = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.signikanegative_regular)),
        fontSize = 20.sp,
        color = regularBlue
    )
)

val regularSignikanegativeWhite20 = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.signikanegative_regular)),
        fontSize = 20.sp,
        color = Color.White
    )
)

val regularSignikanegativeLightGray20 = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.signikanegative_regular)),
        fontSize = 20.sp,
        color = Color.LightGray
    )
)

val regularSignikanegativeBlue30 = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.signikanegative_regular)),
        fontSize = 30.sp,
        color = regularBlue
    )
)