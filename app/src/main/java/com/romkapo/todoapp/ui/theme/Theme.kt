package com.romkapo.todoapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = HakiLight,
    onPrimary = DarkGrey,
    primaryContainer = Aqua,
    secondary = DarkGrey,
    tertiary = Aqua,
    surface = DarkGrey,
    background = LightBlack,
    surfaceContainerHigh = DarkGrey,
    surfaceVariant = DarkGrey,
)

private val LightColorScheme = lightColorScheme(
    primary = HakiDark,
    onPrimary = LightGrey,
    primaryContainer = Aqua,
    secondary = White,
    tertiary = Aqua,
    surface = LightGrey,
    background = LightGrey,
    surfaceContainerHigh = LightGrey,
    surfaceVariant = LightGrey,

)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

@Preview
@Composable
fun LightPalette() {
    val colors = mapOf(
        "primary" to HakiDark,
        "onPrimary" to LightGrey,
        "primaryContainer" to Aqua,
        "secondary" to White,
        "tertiary" to Aqua,
        "surface" to LightGrey,
        "background" to LightGrey,
        "surfaceContainerHigh" to LightGrey,
        "surfaceVariant" to Grey,
    )
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(colors.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(colors.values.toList()[it]),
            ) {
                Text(text = colors.keys.toList()[it])
            }
        }
    }
}

@Preview
@Composable
fun DarkPalette() {
    val colors = mapOf(
        "primary" to HakiLight,
        "onPrimary" to DarkGrey,
        "primaryContainer" to Aqua,
        "secondary" to DarkGrey,
        "tertiary" to Aqua,
        "surface" to DarkGrey,
        "background" to LightGrey,
        "surfaceContainerHigh" to DarkGrey,
        "surfaceVariant" to Grey,
    )
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(colors.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(colors.values.toList()[it]),
            ) {
                Text(text = colors.keys.toList()[it])
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Typographies() {
    Column {
        Text(style = MaterialTheme.typography.titleLarge, text = "What is it")
        Text(style = MaterialTheme.typography.titleMedium, text = "What is it")
        Text(style = MaterialTheme.typography.labelMedium, text = "What is it")
        Text(style = MaterialTheme.typography.bodyMedium, text = "What is it")
        Text(style = MaterialTheme.typography.bodySmall, text = "What is it")
    }
}
