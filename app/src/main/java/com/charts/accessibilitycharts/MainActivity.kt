package com.charts.accessibilitycharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import com.charts.accessibilitycharts.ui.theme.AccessibilityChartsTheme
import com.charts.line.LinearChartScreen
import androidx.compose.ui.unit.dp
import com.charts.bar.BarChart
import com.charts.circle.CircleChartData
import com.charts.circle.CircleChartScreen
import com.charts.circle.CircleData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessibilityChartsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        LinearChartScreen(data = mockData())
                        Spacer(modifier = Modifier.height(16.dp))
                        BarChart()
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
            value = 40f
        ),
        circleB = CircleData(
            title = "Garlic Bread",
            value = 10f
        )
    )
}

