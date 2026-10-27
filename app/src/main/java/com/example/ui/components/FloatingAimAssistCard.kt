package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AvailableColors
import com.example.model.CrosshairConfig
import com.example.model.CrosshairStyle
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FloatingAimAssistCard(
  config: CrosshairConfig,
  onStyleSelected: (CrosshairStyle) -> Unit,
  onColorSelected: (String) -> Unit,
  onSizeChanged: (Float) -> Unit,
  onOpacityChanged: (Float) -> Unit,
  onToggleEnabled: (Boolean) -> Unit,
  onToggleQuickHide: (Boolean) -> Unit,
  onCloseOrMinimize: () -> Unit,
  modifier: Modifier = Modifier,
  isMinimized: Boolean = false,
  onExpand: () -> Unit = {}
) {
  if (isMinimized) {
    // Sleek floating pill when minimized
    Box(
      modifier = modifier
        .testTag("floating_aim_minimized_pill")
        .clip(RoundedCornerShape(20.dp))
        .background(
          Brush.linearGradient(
            listOf(Color(0xE6161622), Color(0xE625121A))
          )
        )
        .border(1.2.dp, NeonRed.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
        .clickable { onExpand() }
        .padding(horizontal = 14.dp, vertical = 8.dp),
      contentAlignment = Alignment.Center
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CrosshairRenderer(
          config = config.copy(sizeDp = 18f, opacity = 1f),
          modifier = Modifier.size(18.dp)
        )
        Text(
          text = "AIM ASSIST",
          color = TextPrimary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (config.enabled && !config.quickHide) Color(0xFF00E676) else Color.Red)
        )
      }
    }
    return
  }

  // Full Glassmorphic Control Panel exact to screenshot 1
  Box(
    modifier = modifier
      .testTag("floating_aim_card")
      .shadow(16.dp, RoundedCornerShape(18.dp), ambientColor = NeonRed, spotColor = Color.Black)
      .clip(RoundedCornerShape(18.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xF0181824),
            Color(0xF212121A),
            Color(0xF50C0C12)
          )
        )
      )
      .border(
        width = 1.2.dp,
        brush = Brush.verticalGradient(
          listOf(NeonRed.copy(alpha = 0.6f), Color(0x3300E5FF), Color(0x22FFFFFF))
        ),
        shape = RoundedCornerShape(18.dp)
      )
      .padding(16.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Header: "Aiming Assist", switch, and close "X"
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "Aiming Assist",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          NeonSwitch(
            checked = config.enabled,
            onCheckedChange = onToggleEnabled,
            testTag = "toggle_aim_assist_enabled"
          )

          IconButton(
            onClick = onCloseOrMinimize,
            modifier = Modifier.size(32.dp).testTag("close_aim_assist_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Fechar / Minimizar",
              tint = TextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      // Quick Hide row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "Quick Hide",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
          )
          Icon(
            imageVector = Icons.Default.HelpOutline,
            contentDescription = "Ajuda Quick Hide",
            tint = TextMuted,
            modifier = Modifier.size(15.dp)
          )
        }

        NeonSwitch(
          checked = config.quickHide,
          onCheckedChange = onToggleQuickHide,
          testTag = "toggle_quick_hide"
        )
      }

      // Style row
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = "Style",
          color = TextSecondary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          CrosshairStyle.entries.forEach { style ->
            val isSelected = config.style == style
            Box(
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("style_button_${style.name.lowercase()}")
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Color(0xFF330912) else Color(0xFF1E1E28))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) NeonRed else Color(0xFF2C2C3D),
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { onStyleSelected(style) },
              contentAlignment = Alignment.Center
            ) {
              // Mini crosshair preview icon
              CrosshairRenderer(
                config = config.copy(
                  style = style,
                  sizeDp = 22f,
                  opacity = if (isSelected) 1f else 0.7f,
                  quickHide = false,
                  enabled = true
                ),
                modifier = Modifier.size(22.dp)
              )
            }
          }
        }
      }

      // Colour row
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = "Colour",
          color = TextSecondary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          AvailableColors.forEach { item ->
            val isSelected = config.colorHex.equals(item.hex, ignoreCase = true)
            Box(
              modifier = Modifier
                .size(36.dp)
                .testTag("color_button_${item.name.lowercase()}")
                .clip(CircleShape)
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) item.color else Color(0xFF333345),
                  shape = CircleShape
                )
                .clickable { onColorSelected(item.hex) }
                .padding(3.dp),
              contentAlignment = Alignment.Center
            ) {
              Box(
                modifier = Modifier
                  .size(if (isSelected) 24.dp else 22.dp)
                  .shadow(
                    elevation = if (isSelected) 6.dp else 0.dp,
                    shape = CircleShape,
                    ambientColor = item.color,
                    spotColor = item.color
                  )
                  .clip(CircleShape)
                  .background(item.color)
              )
            }
          }
        }
      }

      // Size Slider
      NeonSlider(
        label = "Size",
        value = config.sizeDp,
        onValueChange = onSizeChanged,
        valueRange = 12f..56f,
        valueText = "${config.sizeDp.toInt()} px",
        testTag = "slider_crosshair_size"
      )

      // Opacity Slider
      NeonSlider(
        label = "Opacity",
        value = config.opacity,
        onValueChange = onOpacityChanged,
        valueRange = 0.2f..1.0f,
        valueText = "${(config.opacity * 100).toInt()}%",
        testTag = "slider_crosshair_opacity"
      )
    }
  }
}
