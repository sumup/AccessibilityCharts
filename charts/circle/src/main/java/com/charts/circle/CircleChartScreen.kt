package com.charts.circle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircleChartScreen(
    lineColor: Color = Color.Black,
    fillColor: Color = Color.White,
    backgroundColor: Color = Color.White,
    circleChartData: CircleChartData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(align = Alignment.TopCenter)
        ) {
            CircleChart(
                lineColor = lineColor,
                fillColor = fillColor,
                circleChartData = circleChartData
            )
        }
    }
}

@Composable
fun CircleChart(
    lineColor: Color = Color.Black,
    fillColor: Color = Color.White,
    circleChartData: CircleChartData
) {
    Column {
        Text(
            text = circleChartData.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Box {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = circleChartData.largeCircle().title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = circleChartData.largeCircle().value.toInt().toString(),
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(-45f)
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Circle(
                    lineColor = lineColor,
                    fillColor = lineColor,
                    ratio = circleChartData.largeRatio()
                )
                Circle(
                    lineColor = lineColor,
                    fillColor = fillColor,
                    ratio = circleChartData.smallRatio()
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Text(
                    text = circleChartData.smallCircle().title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = circleChartData.smallCircle().value.toInt().toString(),
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun Circle(
    lineColor: Color,
    fillColor: Color,
    ratio: Float
) {
    Box(
        modifier = Modifier
            .size(150.dp * ratio)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = lineColor,
                shape = CircleShape
            )
            .background(fillColor)
    )
}