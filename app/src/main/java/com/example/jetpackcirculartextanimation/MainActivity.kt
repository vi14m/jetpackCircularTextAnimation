package com.example.jetpackcirculartextanimation

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import com.example.jetpackcirculartextanimation.ui.theme.JetpackCircularTextAnimationTheme
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackCircularTextAnimationTheme {
                RotatingCircularText(
                    text = "Congratulation",
                    initialTextSize = 24.dp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun RotatingCircularText(
    text: String,
    initialTextSize: androidx.compose.ui.unit.Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteRotation = rememberInfiniteTransition(label = "")
    val rotation by infiniteRotation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val infiniteSize = rememberInfiniteTransition(label = "")
    val animatedTextSize by infiniteSize.animateFloat(
        initialValue = (initialTextSize.value - 10f).coerceAtLeast(0f),
        targetValue = (initialTextSize.value + 20f).coerceAtMost(100f),
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val radius = with(LocalDensity.current) { 100.dp.toPx() }
    val textSizePx = with(LocalDensity.current) { animatedTextSize.dp.toPx() }

    val textPaint = remember {
        Paint().apply {
            this.color = color.toArgb()
            this.isAntiAlias = true
            this.style = Paint.Style.FILL
        }
    }

    textPaint.textSize = textSizePx

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val charAngle = 360f / text.length

        rotate(rotation, Offset(centerX, centerY)) {
            for (i in text.indices) {
                val angle = i * charAngle - 90
                val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val y = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

                rotate(degrees = angle + 90, pivot = Offset(x, y)) {
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            text[i].toString(),
                            x,
                            y,
                            textPaint
                        )
                    }
                }
            }
        }
    }
}