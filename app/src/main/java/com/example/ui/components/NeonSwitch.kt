package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow

@Composable
fun NeonSwitch(
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  testTag: String = "neon_switch"
) {
  val interactionSource = remember { MutableInteractionSource() }

  val trackColor by animateColorAsState(
    targetValue = if (checked) Color(0xFF550512) else Color(0xFF1E1E28),
    animationSpec = tween(durationMillis = 220),
    label = "trackColor"
  )

  val borderColor by animateColorAsState(
    targetValue = if (checked) NeonRed.copy(alpha = 0.85f) else Color(0xFF333345),
    animationSpec = tween(durationMillis = 220),
    label = "borderColor"
  )

  val thumbOffset by animateFloatAsState(
    targetValue = if (checked) 1f else 0f,
    animationSpec = tween(durationMillis = 220),
    label = "thumbOffset"
  )

  val thumbColor by animateColorAsState(
    targetValue = if (checked) Color(0xFFFFFFFF) else Color(0xFF6B6B7C),
    animationSpec = tween(durationMillis = 220),
    label = "thumbColor"
  )

  Box(
    modifier = modifier
      .minimumInteractiveComponentSize()
      .testTag(testTag)
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled
      ) {
        onCheckedChange(!checked)
      },
    contentAlignment = Alignment.Center
  ) {
    // Outer Track
    Box(
      modifier = Modifier
        .width(52.dp)
        .height(28.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(trackColor)
        .border(1.2.dp, borderColor, RoundedCornerShape(14.dp))
        .padding(horizontal = 3.dp),
      contentAlignment = Alignment.CenterStart
    ) {
      // Glow when checked
      if (checked) {
        Box(
          modifier = Modifier
            .matchParentSize()
            .background(NeonRedGlow.copy(alpha = 0.15f))
        )
      }

      // Thumb
      val maxShift = 24.dp
      Box(
        modifier = Modifier
          .offset(x = maxShift * thumbOffset)
          .size(22.dp)
          .shadow(
            elevation = if (checked) 6.dp else 2.dp,
            shape = CircleShape,
            ambientColor = if (checked) NeonRed else Color.Black,
            spotColor = if (checked) NeonRed else Color.Black
          )
          .clip(CircleShape)
          .background(thumbColor)
          .border(
            width = if (checked) 1.dp else 0.5.dp,
            color = if (checked) NeonRed else Color.Transparent,
            shape = CircleShape
          )
      )
    }
  }
}
