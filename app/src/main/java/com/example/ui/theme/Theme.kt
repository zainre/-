package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val MonoColorScheme =
  darkColorScheme(
    primary = MonoWhite,
    secondary = MonoLightGray,
    tertiary = MonoGray,
    background = MonoBlack,
    surface = MonoDarkerGray,
    surfaceVariant = MonoDarkGray,
    onPrimary = MonoBlack,
    onSecondary = MonoBlack,
    onTertiary = MonoWhite,
    onBackground = MonoWhite,
    onSurface = MonoWhite,
    onSurfaceVariant = MonoGray,
    error = MonoError,
    outline = MonoGray,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Apple/Twitter-like monochrome theme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = MonoColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
