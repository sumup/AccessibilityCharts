package com.charts.circle

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
    var isBrokenHeartVisible by remember { mutableStateOf(false) }
    var isHeartVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = circleChartData.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(
                onClick = {
                    Toast
                        .makeText(context, "play na musica", Toast.LENGTH_SHORT)
                        .show()
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_music),
                    contentDescription = null
                )
            }
        }
        Box {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 32.dp)
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
                        contentDescription = "",
                        Modifier.padding(top = 16.dp).alpha(alpha)
                    )
                }
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
                    ratio = circleChartData.largeRatio(),
                    backgroundRes = circleChartData.largeCircle().backgroundRes,
                    isVisible = isHeartVisible,
                    onClick = {
                        isHeartVisible = isHeartVisible.not()
                        isBrokenHeartVisible = false
                    }
                )
                Circle(
                    lineColor = lineColor,
                    fillColor = fillColor,
                    ratio = circleChartData.smallRatio(),
                    backgroundRes = circleChartData.smallCircle().backgroundRes,
                    isVisible = isBrokenHeartVisible,
                    onClick = {
                        isBrokenHeartVisible = isBrokenHeartVisible.not()
                        isHeartVisible = false
                    }
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Text(
                    text = circleChartData.smallCircle().title,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 280.dp)
                )
                Row {
                    Text(
                        text = circleChartData.smallCircle().value.toInt().toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    val alpha by animateFloatAsState(
                        animationSpec = tween(durationMillis = 250),
                        targetValue = if (isBrokenHeartVisible) 1f else 0f
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_broken_heart),
                        contentDescription = "",
                        Modifier.padding(top = 12.dp).alpha(alpha)
                    )
                }
            }
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
            .clickable {
                onClick.invoke()
            }
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "Yay",
            modifier = Modifier.alpha(alpha)
        )
    }
}