package com.charts.circle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircleChartScreen(
    lineColor: Color = Color.Black,
    fillColor: Color = Color.White,
    backgroundColor: Color = Color.White,
    valueA: Float,
    valueB: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(align = Alignment.BottomStart)
        ) {
            CircleChart(
                lineColor = lineColor,
                fillColor = fillColor,
                valueA = valueA,
                valueB = valueB
            )
        }
    }
}

@Composable
fun CircleChart(
    lineColor: Color = Color.Black,
    fillColor: Color = Color.White,
    valueA: Float,
    valueB: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .rotate(-45f)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Circle(
            lineColor = lineColor,
            fillColor = lineColor,
            value = maxOf(valueA / valueB, valueB / valueA)
        )
        Circle(
            lineColor = lineColor,
            fillColor = fillColor,
            value = minOf(valueA / valueB, valueB / valueA)
        )
    }
}

@Composable
fun Circle(
    lineColor: Color,
    fillColor: Color,
    value: Float
) {
    Box(
        modifier = Modifier
            .size(100.dp * value)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = lineColor,
                shape = CircleShape
            )
            .background(fillColor)
    )
}