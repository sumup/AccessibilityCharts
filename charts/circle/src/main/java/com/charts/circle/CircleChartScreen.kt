package com.charts.circle

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
            .padding(24.dp),
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
                    AnimatedVisibility(
                        visible = isHeartVisible,
                        enter = fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = fadeOut(
                            animationSpec = tween(durationMillis = 250)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_heart),
                            contentDescription = "",
                            Modifier.padding(top = 16.dp)
                        )
                    }
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
                    onClick = {
                        isHeartVisible = isHeartVisible.not()
                    }
                )
                Circle(
                    lineColor = lineColor,
                    fillColor = fillColor,
                    ratio = circleChartData.smallRatio(),
                    backgroundRes = circleChartData.smallCircle().backgroundRes,
                    onClick = {
                        isBrokenHeartVisible = isBrokenHeartVisible.not()
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
                    AnimatedVisibility(
                        visible = isBrokenHeartVisible,
                        enter = fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = fadeOut(
                            animationSpec = tween(durationMillis = 250)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_broken_heart),
                            contentDescription = "",
                            Modifier.padding(top = 12.dp)
                        )
                    }
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
    @DrawableRes backgroundRes: Int,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

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
                visible = visible.not()
                onClick.invoke()
            }
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                initialAlpha = 0.3f
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            Image(painter = painterResource(id = backgroundRes), contentDescription = "Yay")
        }
    }
}