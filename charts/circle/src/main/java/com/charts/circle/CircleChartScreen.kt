package com.charts.circle

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charts.player.AudioPlayer
import kotlinx.coroutines.delay

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
            .padding(16.dp)
            .semantics {
                contentDescription =
                    "Best Sellers vs Least Sellers Circular Chart for January 7, 2022."
            },
        shape = RoundedCornerShape(8),
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        CircleChart(
            lineColor = lineColor,
            fillColor = fillColor,
            circleChartData = circleChartData
        )
    }
}

@Composable
fun CircleChart(
    lineColor: Color,
    fillColor: Color,
    circleChartData: CircleChartData
) {
    var isBrokenHeartVisible by remember { mutableStateOf(false) }
    var isHeartVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val minValue = circleChartData.smallCircle().value.toDouble()
    val maxValue = circleChartData.largeCircle().value.toDouble()
    var playSoundTimes by remember {
        mutableStateOf(0)
    }
    var values by remember {
        mutableStateOf(listOf(maxValue, minValue))
    }

    Column(Modifier.padding(start = 16.dp)) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 24.dp, top = 24.dp)
            ) {
                Text(
                    text = circleChartData.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "January 7, 2022",
                    fontSize = 14.sp,
                    color = Color(0xFF999999)
                )
            }
            IconButton(
                onClick = {
                    values = listOf(maxValue, minValue)
                    playSoundTimes += 1
                },
                Modifier
                    .padding(top = 24.dp, end = 8.dp)
                    .semantics {
                        contentDescription = "Play the sound of the best and least sold."
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_music),
                    contentDescription = null
                )
            }
        }
        Box(
            modifier = Modifier.semantics(mergeDescendants = true) {
                contentDescription =
                    "The larger circle in black represents the most sold product, Cappuccino, with 40 units sold. The smaller circle in white color and black border represents the least sold product, Cheesecake, with 8 units sold."
            }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clearAndSetSemantics { }
            ) {
                Text(
                    text = circleChartData.largeCircle().title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Row {
                    Text(
                        text = circleChartData.largeCircle().value.toInt().toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    val alpha by animateFloatAsState(
                        animationSpec = tween(durationMillis = 250),
                        targetValue = if (isHeartVisible) 1f else 0f
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = null,
                        Modifier
                            .padding(top = 16.dp)
                            .alpha(alpha)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(-45f)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Circle(
                    lineColor = lineColor,
                    fillColor = lineColor,
                    ratio = circleChartData.largeRatio(),
                    backgroundRes = circleChartData.largeCircle().backgroundRes,
                    isVisible = isHeartVisible,
                    onClick = {
                        values = listOf(maxValue)
                        playSoundTimes += 1
                    }
                )
                Circle(
                    lineColor = lineColor,
                    fillColor = fillColor,
                    ratio = circleChartData.smallRatio(),
                    backgroundRes = circleChartData.smallCircle().backgroundRes,
                    isVisible = isBrokenHeartVisible,
                    onClick = {
                        values = listOf(minValue)
                        playSoundTimes += 1
                    }
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(top = 232.dp, end = 16.dp)
                    .clearAndSetSemantics { }
            ) {
                Text(
                    text = circleChartData.smallCircle().title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Row {
                    Text(
                        text = circleChartData.smallCircle().value.toInt().toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val alpha by animateFloatAsState(
                        animationSpec = tween(durationMillis = 250),
                        targetValue = if (isBrokenHeartVisible) 1f else 0f
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_broken_heart),
                        contentDescription = null,
                        Modifier
                            .padding(top = 12.dp)
                            .alpha(alpha)
                    )
                }
            }
        }
    }

    if (playSoundTimes > 0) {
        LaunchedEffect(key1 = playSoundTimes) {
            val player = AudioPlayer(context)

            player.updateLowHighPoints(minValue, maxValue)
            values.forEach { value ->
                delay(700)
                if (value == maxValue) {
                    isHeartVisible = true
                    isBrokenHeartVisible = false
                } else if (value == minValue) {
                    isHeartVisible = false
                    isBrokenHeartVisible = true
                }
                player.onPointFocused(1.0, value)
            }
            delay(700)
            isHeartVisible = false
            isBrokenHeartVisible = false

            player.dispose()
        }
    }
}

@Composable
fun Circle(
    lineColor: Color,
    fillColor: Color,
    ratio: Float,
    isVisible: Boolean,
    @DrawableRes backgroundRes: Int,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        animationSpec = tween(durationMillis = 250),
        targetValue = if (isVisible) 1f else 0f
    )
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(170.dp * ratio)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = lineColor,
                shape = CircleShape
            )
            .background(fillColor)
            .clearAndSetSemantics { }
            .clickable(
                enabled = context
                    .isAccessibilityEnabled()
                    .not()
            ) {
                onClick.invoke()
            }
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            modifier = Modifier.alpha(alpha)
        )
    }
}

fun Context.isAccessibilityEnabled(): Boolean =
    (getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager)?.let {
        val serviceInfoList =
            it.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN)
        serviceInfoList?.isNotEmpty() ?: false
    } ?: false