package com.example.globalchat.view

import android.graphics.Paint
import android.graphics.PathEffect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WaveWithDepression(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val radius = width / 2

        val path = Path().apply {
            // Draw a top semicircle centered horizontally
            arcTo(
                rect = Rect(
                    offset = Offset(0f, 0f),
                    size = Size(width, radius)
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f, // negative to curve downward
                forceMoveTo = false
            )

            // Close the path to the bottom
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(path = path, color = Color.Cyan)
    }
    }


@Preview(showBackground = true)
@Composable
fun PreviewWaveBackgroundScreen() {
    WaveWithDepression()
}
