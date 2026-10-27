package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CrosshairConfig
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class FloatingDamage(
  val id: Long,
  val text: String,
  val isHeadshot: Boolean,
  val x: Float,
  val y: Float
)

@Composable
fun TargetTestRange(
  config: CrosshairConfig,
  hits: Int,
  headshots: Int,
  onTargetHit: (isHeadshot: Boolean) -> Unit,
  onResetStats: () -> Unit,
  onDragCrosshair: (dx: Float, dy: Float) -> Unit,
  onResetPosition: () -> Unit,
  modifier: Modifier = Modifier
) {
  val damages = remember { mutableStateListOf<FloatingDamage>() }
  val scope = rememberCoroutineScope()

  // Moving target sway animation
  val infiniteTransition = rememberInfiniteTransition(label = "target_sway")
  val swayOffset by infiniteTransition.animateFloat(
    initialValue = -35f,
    targetValue = 35f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sway"
  )

  BoxWithConstraints(
    modifier = modifier
      .fillMaxWidth()
      .height(280.dp)
      .testTag("target_test_range")
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.verticalGradient(
          listOf(Color(0xFF0F1420), Color(0xFF07090E))
        )
      )
      .border(1.dp, Color(0xFF232A3D), RoundedCornerShape(16.dp))
  ) {
    val canvasWidth = constraints.maxWidth.toFloat()
    val canvasHeight = constraints.maxHeight.toFloat()
    val centerX = canvasWidth / 2f
    val centerY = canvasHeight / 2f

    // Background tactical grid
    Canvas(modifier = Modifier.fillMaxSize()) {
      val gridStep = 32.dp.toPx()
      var x = 0f
      while (x <= size.width) {
        drawLine(
          color = Color(0x1800E5FF),
          start = Offset(x, 0f),
          end = Offset(x, size.height),
          strokeWidth = 1f
        )
        x += gridStep
      }
      var y = 0f
      while (y <= size.height) {
        drawLine(
          color = Color(0x1800E5FF),
          start = Offset(0f, y),
          end = Offset(size.width, y),
          strokeWidth = 1f
        )
        y += gridStep
      }

      // Tactical HUD range rings
      drawCircle(
        color = Color(0x2200E5FF),
        radius = size.height * 0.42f,
        center = Offset(centerX, centerY),
        style = Stroke(width = 1.2f)
      )
      drawCircle(
        color = Color(0x33FF0033),
        radius = size.height * 0.22f,
        center = Offset(centerX, centerY),
        style = Stroke(width = 1.2f)
      )
    }

    // Target Mannequin Box (Interactive Hitbox)
    val mannequinCenterX = centerX + swayOffset
    val mannequinCenterY = centerY

    Box(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTapGestures { tapOffset ->
            val distToHead = kotlin.math.hypot(
              tapOffset.x - mannequinCenterX,
              tapOffset.y - (mannequinCenterY - 45f)
            )
            val distToBody = kotlin.math.hypot(
              tapOffset.x - mannequinCenterX,
              tapOffset.y - mannequinCenterY
            )

            val isHs = distToHead < 42f
            val isBody = distToBody < 80f

            if (isHs || isBody) {
              val damageVal = if (isHs) (180..220).random() else (32..48).random()
              val damageText = if (isHs) "💥 $damageVal HS!" else "$damageVal"
              val item = FloatingDamage(
                id = System.currentTimeMillis(),
                text = damageText,
                isHeadshot = isHs,
                x = tapOffset.x,
                y = tapOffset.y
              )
              damages.add(item)
              onTargetHit(isHs)

              scope.launch {
                delay(900)
                damages.remove(item)
              }
            }
          }
        }
    ) {
      // Draw Mannequin
      Canvas(modifier = Modifier.fillMaxSize()) {
        val headCenter = Offset(mannequinCenterX, mannequinCenterY - 45f)
        val headRadius = 24.dp.toPx()

        // Glow headshot zone (Red)
        drawCircle(
          color = NeonRed.copy(alpha = 0.25f),
          radius = headRadius * 1.3f,
          center = headCenter
        )
        drawCircle(
          color = Color(0xFF450A14),
          radius = headRadius,
          center = headCenter
        )
        drawCircle(
          color = NeonRed,
          radius = headRadius,
          center = headCenter,
          style = Stroke(width = 2.dp.toPx())
        )

        // Chest/Shoulders
        val chestTop = headCenter.y + headRadius
        drawRoundRect(
          color = Color(0xFF162030),
          topLeft = Offset(mannequinCenterX - 36.dp.toPx(), chestTop + 4.dp.toPx()),
          size = Size(72.dp.toPx(), 70.dp.toPx()),
          cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()),
          style = Stroke(width = 2.dp.toPx())
        )
      }

      // Damage floaters
      damages.forEach { dmg ->
        Box(
          modifier = Modifier
            .offset { IntOffset(dmg.x.toInt() - 40, (dmg.y - 30).toInt()) }
        ) {
          Text(
            text = dmg.text,
            color = if (dmg.isHeadshot) NeonRed else NeonYellow,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (dmg.isHeadshot) 16.sp else 13.sp
          )
        }
      }

      // Draggable Crosshair Overlay Box
      Box(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
              change.consume()
              onDragCrosshair(dragAmount.x, dragAmount.y)
            }
          },
        contentAlignment = Alignment.Center
      ) {
        CrosshairRenderer(
          config = config,
          modifier = Modifier
            .offset {
              IntOffset(config.floatingX.roundToInt(), config.floatingY.roundToInt())
            }
            .testTag("active_crosshair_in_range")
        )
      }
    }

    // Top HUD Bar inside Target Range
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xCC0E0E18))
            .border(1.dp, Color(0x4400E5FF), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "STAND DE TIRO VIP",
            color = NeonCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Text(
          text = "🎯 Toque no alvo para testar HS",
          color = TextSecondary,
          fontSize = 11.sp
        )
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onResetPosition,
          modifier = Modifier.size(28.dp).testTag("recenter_crosshair_button")
        ) {
          Icon(
            imageVector = Icons.Default.CenterFocusStrong,
            contentDescription = "Recentralizar Mira",
            tint = NeonCyan,
            modifier = Modifier.size(16.dp)
          )
        }

        IconButton(
          onClick = onResetStats,
          modifier = Modifier.size(28.dp).testTag("reset_stats_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Zerar Estatísticas",
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    // Bottom Stats Overlay
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .background(Color(0xBB080A12))
        .padding(horizontal = 12.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      val hsRate = if (hits > 0) ((headshots.toFloat() / hits) * 100).toInt() else 0

      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
          text = "Acertos: $hits",
          color = TextPrimary,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
        Text(
          text = "💥 HS: $headshots",
          color = NeonRed,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Taxa HS: $hsRate%",
          color = NeonYellow,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Text(
        text = "Arraste para reposicionar",
        color = TextMuted,
        fontSize = 10.sp
      )
    }
  }
}
