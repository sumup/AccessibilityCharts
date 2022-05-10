package com.hackweeek.accessibilitycharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.hackweeek.accessibilitycharts.linearchart.LinearChartScreen
import com.hackweeek.accessibilitycharts.ui.theme.AccessibilityChartsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessibilityChartsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LinearChartScreen()
                }
            }
        }
    }
}

