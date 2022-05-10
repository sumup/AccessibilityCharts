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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessibilityChartsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LinearChartScreen(data = mockData())
                }
            }
        }
    }

    private fun mockData() = listOf(5, 6, 8, 5, 7, 6, 3, 5, 4, 7, 4, 5, 3, 7, 9, 12, 11, 5, 4, 5, 11, 8, 12, 10, 11, 7, 6, 4, 9, 7)
}

