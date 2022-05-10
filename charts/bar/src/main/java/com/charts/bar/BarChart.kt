package com.charts.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BarChart() {
    Row {
        Bar()
        Spacer(modifier = Modifier.width(16.dp))
        Bar()
        Spacer(modifier = Modifier.width(16.dp))
        Bar()
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun Bar() {
    Box(
        modifier = Modifier
            .size(width = 50.dp, height = 100.dp)
            .background(Color.Black)
    )
}

@Preview
@Composable
fun BarChartPreview() {
    BarChart()
}
