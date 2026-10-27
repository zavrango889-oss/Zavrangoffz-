package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.CrosshairConfig
import com.example.model.CrosshairStyle

@Composable
fun CrosshairRenderer(
  config: CrosshairConfig,
  modifier: Modifier = Modifier
) {
  if (!config.enabled || config.quickHide) return

  val crosshairColor = try {
    Color(android.graphics.Color.parseColor(config.colorHex)).copy(alpha = config.opacity)
  } catch (_: Exception) {
    Color.Red.copy(alpha = config.opacity)
  }

  val displaySizeDp = config.sizeDp.dp

  Canvas(modifier = modifier.size(displaySizeDp)) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val strokeWidth = (size.width * 0.08f).coerceIn(2f, 6f)
    val glowColor = crosshairColor.copy(alpha = config.opacity * 0.35f)

    when (config.style) {
      CrosshairStyle.DOT -> {
        val radius = size.width * 0.22f
        // Ambient glow
        drawCircle(color = glowColor, radius = radius * 1.8f, center = center)
        // Solid center dot
        drawCircle(color = crosshairColor, radius = radius, center = center)
        // Center white specular pinpoint
        drawCircle(color = Color.White.copy(alpha = config.opacity * 0.8f), radius = radius * 0.35f, center = center)
      }

      CrosshairStyle.CIRCLE -> {
        val radius = size.width * 0.35f
        // Outer glow
        drawCircle(
          color = glowColor,
          radius = radius,
          center = center,
          style = Stroke(width = strokeWidth * 2.2f)
        )
        // Main circle
        drawCircle(
          color = crosshairColor,
          radius = radius,
          center = center,
          style = Stroke(width = strokeWidth)
        )
        // Center pinpoint
        drawCircle(color = crosshairColor, radius = strokeWidth * 0.9f, center = center)

        // 4 outer subtle ticks
        val tickLength = size.width * 0.12f
        drawLine(crosshairColor, Offset(center.x, center.y - radius), Offset(center.x, center.y - radius - tickLength), strokeWidth)
        drawLine(crosshairColor, Offset(center.x, center.y + radius), Offset(center.x, center.y + radius + tickLength), strokeWidth)
        drawLine(crosshairColor, Offset(center.x - radius, center.y), Offset(center.x - radius - tickLength, center.y), strokeWidth)
        drawLine(crosshairColor, Offset(center.x + radius, center.y), Offset(center.x + radius + tickLength, center.y), strokeWidth)
      }

      CrosshairStyle.CROSSHAIR -> {
        val gap = size.width * 0.14f
        val armLength = size.width * 0.38f

        // Top arm
        drawLine(crosshairColor, Offset(center.x, center.y - gap), Offset(center.x, center.y - gap - armLength), strokeWidth, StrokeCap.Round)
        // Bottom arm
        drawLine(crosshairColor, Offset(center.x, center.y + gap), Offset(center.x, center.y + gap + armLength), strokeWidth, StrokeCap.Round)
        // Left arm
        drawLine(crosshairColor, Offset(center.x - gap, center.y), Offset(center.x - gap - armLength, center.y), strokeWidth, StrokeCap.Round)
        // Right arm
        drawLine(crosshairColor, Offset(center.x + gap, center.y), Offset(center.x + gap + armLength, center.y), strokeWidth, StrokeCap.Round)

        // Center micro-dot
        drawCircle(color = crosshairColor, radius = strokeWidth * 0.6f, center = center)
      }

      CrosshairStyle.BOX_DOT -> {
        val half = size.width * 0.38f
        val bracketLen = size.width * 0.16f

        // Glow
        drawRect(
          color = glowColor,
          topLeft = Offset(center.x - half, center.y - half),
          size = Size(half * 2, half * 2),
          style = Stroke(width = strokeWidth * 1.8f)
        )

        // 4 tactical corner brackets:
        // Top-Left
        drawLine(crosshairColor, Offset(center.x - half, center.y - half), Offset(center.x - half + bracketLen, center.y - half), strokeWidth, StrokeCap.Square)
        drawLine(crosshairColor, Offset(center.x - half, center.y - half), Offset(center.x - half, center.y - half + bracketLen), strokeWidth, StrokeCap.Square)

        // Top-Right
        drawLine(crosshairColor, Offset(center.x + half, center.y - half), Offset(center.x + half - bracketLen, center.y - half), strokeWidth, StrokeCap.Square)
        drawLine(crosshairColor, Offset(center.x + half, center.y - half), Offset(center.x + half, center.y - half + bracketLen), strokeWidth, StrokeCap.Square)

        // Bottom-Left
        drawLine(crosshairColor, Offset(center.x - half, center.y + half), Offset(center.x - half + bracketLen, center.y + half), strokeWidth, StrokeCap.Square)
        drawLine(crosshairColor, Offset(center.x - half, center.y + half), Offset(center.x - half, center.y + half - bracketLen), strokeWidth, StrokeCap.Square)

        // Bottom-Right
        drawLine(crosshairColor, Offset(center.x + half, center.y + half), Offset(center.x + half - bracketLen, center.y + half), strokeWidth, StrokeCap.Square)
        drawLine(crosshairColor, Offset(center.x + half, center.y + half), Offset(center.x + half, center.y + half - bracketLen), strokeWidth, StrokeCap.Square)

        // Center Dot
        drawCircle(color = crosshairColor, radius = strokeWidth * 1.1f, center = center)
      }
    }
  }
}
