package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CrosshairConfig
import com.example.model.CrosshairStyle
import com.example.ui.components.FloatingAimAssistCard
import com.example.ui.components.TargetTestRange
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@Composable
fun AimAssistScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val crosshairConfig by viewModel.crosshairConfig.collectAsStateWithLifecycle()
  val hits by viewModel.hitsCount.collectAsStateWithLifecycle()
  val headshots by viewModel.headshotsCount.collectAsStateWithLifecycle()

  var isCardMinimized by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Bar Status
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(if (crosshairConfig.enabled && !crosshairConfig.quickHide) Color(0xFF00E676) else NeonRed)
        )
        Text(
          text = if (crosshairConfig.enabled && !crosshairConfig.quickHide) "MIRA ATIVA & CALIBRADA" else "MIRA OCULTA / DESATIVADA",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }

      OutlinedButton(
        onClick = { isCardMinimized = !isCardMinimized },
        modifier = Modifier.testTag("toggle_minimize_aim_button"),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = NeonCyan
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
          brush = Brush.horizontalGradient(listOf(NeonCyan, NeonRed))
        )
      ) {
        Icon(
          imageVector = if (isCardMinimized) Icons.Default.Visibility else Icons.Default.VisibilityOff,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isCardMinimized) "Expandir Painel" else "Minimizar",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // Interactive Target Range Canvas (Where user can see & drag the crosshair!)
    TargetTestRange(
      config = crosshairConfig,
      hits = hits,
      headshots = headshots,
      onTargetHit = { isHs -> viewModel.registerTargetHit(isHs) },
      onResetStats = { viewModel.resetTargetStats() },
      onDragCrosshair = { dx, dy -> viewModel.updateFloatingPosition(dx, dy) },
      onResetPosition = { viewModel.resetFloatingPosition() }
    )

    // The Floating Aiming Assist Card (Exact Screenshot 1)
    FloatingAimAssistCard(
      config = crosshairConfig,
      onStyleSelected = { viewModel.setCrosshairStyle(it) },
      onColorSelected = { viewModel.setCrosshairColor(it) },
      onSizeChanged = { viewModel.setCrosshairSize(it) },
      onOpacityChanged = { viewModel.setCrosshairOpacity(it) },
      onToggleEnabled = { viewModel.toggleCrosshairEnabled(it) },
      onToggleQuickHide = { viewModel.toggleQuickHide(it) },
      onCloseOrMinimize = { isCardMinimized = true },
      isMinimized = isCardMinimized,
      onExpand = { isCardMinimized = false }
    )

    // Quick Pro Presets
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CyberCard)
        .border(1.dp, Color(0xFF28283C), RoundedCornerShape(16.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = "PRESETS RÁPIDOS DE MIRA",
        color = NeonRed,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        PresetButton(
          title = "Ponto Pro",
          subtitle = "Dot 22px Red",
          isSelected = crosshairConfig.style == CrosshairStyle.DOT && crosshairConfig.colorHex == "#FF0033",
          modifier = Modifier.weight(1f),
          onClick = {
            viewModel.setCrosshairStyle(CrosshairStyle.DOT)
            viewModel.setCrosshairColor("#FF0033")
            viewModel.setCrosshairSize(22f)
            viewModel.setCrosshairOpacity(1f)
          }
        )

        PresetButton(
          title = "Círculo Cyan",
          subtitle = "Circle 30px Cyan",
          isSelected = crosshairConfig.style == CrosshairStyle.CIRCLE && crosshairConfig.colorHex == "#00E5FF",
          modifier = Modifier.weight(1f),
          onClick = {
            viewModel.setCrosshairStyle(CrosshairStyle.CIRCLE)
            viewModel.setCrosshairColor("#00E5FF")
            viewModel.setCrosshairSize(30f)
            viewModel.setCrosshairOpacity(0.9f)
          }
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        PresetButton(
          title = "Crosshair Verde",
          subtitle = "Cross 28px Green",
          isSelected = crosshairConfig.style == CrosshairStyle.CROSSHAIR && crosshairConfig.colorHex == "#00E676",
          modifier = Modifier.weight(1f),
          onClick = {
            viewModel.setCrosshairStyle(CrosshairStyle.CROSSHAIR)
            viewModel.setCrosshairColor("#00E676")
            viewModel.setCrosshairSize(28f)
            viewModel.setCrosshairOpacity(1f)
          }
        )

        PresetButton(
          title = "Tactical Box",
          subtitle = "Box Dot 34px White",
          isSelected = crosshairConfig.style == CrosshairStyle.BOX_DOT && crosshairConfig.colorHex == "#FFFFFF",
          modifier = Modifier.weight(1f),
          onClick = {
            viewModel.setCrosshairStyle(CrosshairStyle.BOX_DOT)
            viewModel.setCrosshairColor("#FFFFFF")
            viewModel.setCrosshairSize(34f)
            viewModel.setCrosshairOpacity(0.95f)
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun PresetButton(
  title: String,
  subtitle: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(if (isSelected) Color(0xFF3B0B14) else Color(0xFF14141E))
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) NeonRed else Color(0xFF262638),
        shape = RoundedCornerShape(10.dp)
      )
      .clickable { onClick() }
      .padding(horizontal = 10.dp, vertical = 8.dp)
  ) {
    Column {
      Text(
        text = title,
        color = if (isSelected) TextPrimary else TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 10.sp
      )
    }
  }
}
