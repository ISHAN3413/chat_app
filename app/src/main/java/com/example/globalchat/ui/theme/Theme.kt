package com.example.globalchat.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


 val LightColorScheme = lightColorScheme(
    primary = Color(53,	84,	39),
    onPrimary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
     tertiary =Color(106,	168,	79),
     onTertiary = Color(243,	246,	244)
)

// Dark Theme Colors
 val DarkColorScheme = darkColorScheme(
    primary = Color(106,	168,	79),
    onPrimary = Color(190,190,190),
    background = Color.Black,
    onBackground = Color.White,
    tertiary = Color(63,	100,	47),
    onTertiary = Color(10,10,10)
)

// Define Typography
 val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        color = Color.Black
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        color = Color(53,	84,	39)
    )
)

// Define Shapes (Optional)
 val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}