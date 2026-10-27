package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CyberColorScheme = darkColorScheme(
  primary = NeonRed,
  onPrimary = BrightWhite,
  primaryContainer = NeonRedDark,
  onPrimaryContainer = BrightWhite,
  secondary = NeonCyan,
  onSecondary = CyberBlack,
  secondaryContainer = CyberSurface,
  onSecondaryContainer = NeonCyan,
  tertiary = NeonYellow,
  onTertiary = CyberBlack,
  background = CyberBlack,
  onBackground = TextPrimary,
  surface = CyberDark,
  onSurface = TextPrimary,
  surfaceVariant = CyberSurface,
  onSurfaceVariant = TextSecondary,
  outline = CyberCardBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For a true esports HUD gaming aesthetic, retain the custom neon cyber dark palette
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = CyberColorScheme,
    typography = Typography,
    content = content
  )
}
