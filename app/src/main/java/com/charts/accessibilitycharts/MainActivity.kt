package com.charts.accessibilitycharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.charts.accessibilitycharts.ui.theme.AccessibilityChartsTheme
import com.charts.bar.BarChartScreen
import com.charts.circle.CircleChartData
import com.charts.circle.CircleChartScreen
import com.charts.circle.CircleData
import com.charts.line.LinearChartScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessibilityChartsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        LinearChartScreen(data = mockData())
                        Spacer(modifier = Modifier.height(16.dp))
                        BarChartScreen()
                        Spacer(modifier = Modifier.height(16.dp))
                        CircleChartScreen(circleChartData = mockCircleData())
                    }
                }
            }
        }
    }

    private fun mockData() = listOf(5, 8, 9, 12, 3, 6, 2)

    private fun mockCircleData() = CircleChartData(
        title = "Best x Least Seller",
        circleA = CircleData(
            title = "Cappuccino",
            value = 40f,
            backgroundRes = com.charts.circle.R.drawable.cappucino
        ),
        circleB = CircleData(
            title = "Garlic Bread",
            value = 8f,
            backgroundRes = com.charts.circle.R.drawable.garlic_bread
        )
    )
}

