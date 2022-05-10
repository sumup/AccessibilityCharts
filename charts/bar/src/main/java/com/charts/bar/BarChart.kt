package com.charts.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    values: List<Float> = listOf(10f, 50f, 60f, 10f, 50f, 60f, 30f)
) {
    val height = 160
    val maxValue = values.maxOrNull() ?: 0f
    Box(
        Modifier.height(200.dp).fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            Modifier
                .align(Alignment.Center),
            verticalAlignment = Alignment.Bottom
        ) {
            Bar("M", maxValue, height, values[0])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", maxValue, height, values[1])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("W", maxValue, height, values[2])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", maxValue, height, values[3])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("F", maxValue, height, values[4])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", maxValue, height, values[5])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", maxValue, height, values[6])
        }
    }
}

@Composable
fun Bar(text: String, maxValue: Float, height: Int, value: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val barHeight = (value / maxValue) * height
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(barHeight.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black)
        )

        Spacer(Modifier.height(16.dp))
        Text(text = text, color = Color.Black)
    }
}

@Preview
@Composable
fun BarChartPreview() {
    BarChart()
}
