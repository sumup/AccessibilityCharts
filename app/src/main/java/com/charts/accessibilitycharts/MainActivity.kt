package com.charts.accessibilitycharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.charts.accessibilitycharts.ui.theme.AccessibilityChartsTheme
import com.charts.line.LinearChartScreen
import androidx.compose.ui.unit.dp
import com.charts.bar.BarChart

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessibilityChartsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        LinearChartScreen(data = mockData())
                        Spacer(modifier = Modifier.height(16.dp))
                        BarChart()
                    }
                }
            }
        }
    }

    private fun mockData() = listOf(5, 8, 9, 12, 3, 6, 2, 10, 5, 3, 6)
}

