package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CommonDevicePresets
import com.example.model.SensitivityConfig
import com.example.model.WeaponCategory
import com.example.ui.components.NeonSlider
import com.example.ui.components.NeonSwitch
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@Composable
fun ConfigSensiScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val sensiConfig by viewModel.sensitivityConfig.collectAsStateWithLifecycle()
  var isDeviceDropdownOpen by remember { mutableStateOf(false) }

  // Toggles state matching Screenshot 3
  var sensiCelularActive by remember { mutableStateOf(true) }
  var hudMobileActive by remember { mutableStateOf(false) }
  var dpiCelularActive by remember { mutableStateOf(true) }
  var sensiArmaActive by remember { mutableStateOf(true) }
  var miraBasicaActive by remember { mutableStateOf(false) }
  var configMiraActive by remember { mutableStateOf(true) }

  // Pulsing animation for the main power ring
  val infiniteTransition = rememberInfiniteTransition(label = "power_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Banner matching Screenshot 3
    Text(
      text = "START YOUR ALL IN SENSI • ZAVRANGOFFZ",
      color = Color(0xFFFF8599),
      fontSize = 17.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 1.5.sp
    )

    // Center Glowing Power Button matching Screenshot 3
    Box(
      modifier = Modifier
        .size(110.dp)
        .testTag("power_button_sensi"),
      contentAlignment = Alignment.Center
    ) {
      // Outer pulse halo
      Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
          color = NeonRed.copy(alpha = 0.22f),
          radius = (size.minDimension / 2f) * pulseScale,
          style = Stroke(width = 3.dp.toPx())
        )
        drawCircle(
          color = NeonRed.copy(alpha = 0.4f),
          radius = (size.minDimension / 2f) * 0.85f,
          style = Stroke(width = 1.5.dp.toPx())
        )
      }

      // Power Core
      Box(
        modifier = Modifier
          .size(76.dp)
          .shadow(16.dp, CircleShape, ambientColor = NeonRed, spotColor = NeonRed)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              listOf(Color(0xFFFF1744), Color(0xFF88001B), Color(0xFF330009))
            )
          )
          .border(2.dp, NeonRed, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.PowerSettingsNew,
          contentDescription = "Ativar Sensibilidade Otimizada",
          tint = Color.White,
          modifier = Modifier.size(34.dp)
        )
      }
    }

    Text(
      text = "TREINAMENTO ARMADO",
      color = NeonRed,
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 2.sp
    )

    // Device Preset Selection Card
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CyberCard)
        .border(1.dp, Color(0xFF28283C), RoundedCornerShape(16.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "MODELO DE DISPOSITIVO (PRESET)",
        color = NeonCyan,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF12121A))
          .border(1.dp, Color(0xFF35354A), RoundedCornerShape(10.dp))
          .clickable { isDeviceDropdownOpen = true }
          .padding(horizontal = 14.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = sensiConfig.selectedDevice,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = "Alterar ▼",
            color = NeonRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        DropdownMenu(
          expanded = isDeviceDropdownOpen,
          onDismissRequest = { isDeviceDropdownOpen = false },
          modifier = Modifier.background(CyberDark)
        ) {
          CommonDevicePresets.forEach { preset ->
            DropdownMenuItem(
              text = {
                Text(
                  text = "${preset.name} (${preset.recommendedDpi} DPI, ${preset.refreshRate}Hz)",
                  color = TextPrimary,
                  fontSize = 13.sp
                )
              },
              onClick = {
                viewModel.applyDevicePreset(preset.name)
                isDeviceDropdownOpen = false
              }
            )
          }
        }
      }
    }

    // DPI & Refresh Rate Adjustment Card
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CyberCard)
        .border(1.dp, Color(0xFF28283C), RoundedCornerShape(16.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      NeonSlider(
        label = "DPI do Celular (Largura Menor)",
        value = sensiConfig.screenDpi.toFloat(),
        onValueChange = { viewModel.setScreenDpi(it.toInt()) },
        valueRange = 360f..960f,
        valueText = "${sensiConfig.screenDpi} DPI",
        testTag = "slider_dpi"
      )

      // Quick DPI buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        listOf(411, 480, 600, 720, 960).forEach { dpi ->
          val isSelected = sensiConfig.screenDpi == dpi
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) NeonRed else Color(0xFF14141E))
              .clickable { viewModel.setScreenDpi(dpi) }
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "$dpi",
              color = if (isSelected) Color.White else TextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Refresh Rate Selector
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = "Taxa de Atualização da Tela",
          color = TextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(60, 90, 120, 144).forEach { hz ->
            val isSelected = sensiConfig.refreshRateHz == hz
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFF4A0A14) else Color(0xFF14141E))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) NeonRed else Color(0xFF262638),
                  shape = RoundedCornerShape(8.dp)
                )
                .clickable { viewModel.setRefreshRate(hz) }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${hz}Hz",
                color = if (isSelected) Color.White else TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // FOV Adjuster
      NeonSlider(
        label = "Ajuste de FOV (Campo de Visão)",
        value = sensiConfig.fovAngle.toFloat(),
        onValueChange = { viewModel.setFovAngle(it.toInt()) },
        valueRange = 60f..120f,
        valueText = "${sensiConfig.fovAngle}°",
        accentColor = NeonCyan,
        testTag = "slider_fov"
      )
    }

    // Weapon Profile Selector
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
        text = "PERFIL DE SENSIBILIDADE POR ARMA",
        color = NeonRed,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      WeaponCategory.entries.forEach { weapon ->
        val isSelected = sensiConfig.selectedWeapon == weapon
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF3B0B14) else Color(0xFF12121A))
            .border(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) NeonRed else Color(0xFF282838),
              shape = RoundedCornerShape(10.dp)
            )
            .clickable { viewModel.setWeaponCategory(weapon) }
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = weapon.title,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = weapon.subtitle,
                color = TextMuted,
                fontSize = 11.sp
              )
            }

            if (isSelected) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(NeonRed)
              )
            }
          }
        }
      }
    }

    // Calculated Sensi Values Grid
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(
          Brush.verticalGradient(
            listOf(Color(0xFF18101E), Color(0xFF100C16))
          )
        )
        .border(1.dp, Color(0x66FF0033), RoundedCornerShape(16.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "🎯 SENSI CALCULADA (FREE FIRE)",
          color = Color.White,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )

        Button(
          onClick = { viewModel.copySensitivityToClipboard() },
          colors = ButtonDefaults.buttonColors(
            containerColor = NeonRed
          ),
          modifier = Modifier.testTag("copy_sensi_button")
        ) {
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Copiar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }

      // 6 Sensi Cards
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SensiValueCard("Geral", sensiConfig.geral, Modifier.weight(1f))
          SensiValueCard("Ponto Vermelho", sensiConfig.pontoVermelho, Modifier.weight(1f))
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SensiValueCard("Mira 2x", sensiConfig.mira2x, Modifier.weight(1f))
          SensiValueCard("Mira 4x", sensiConfig.mira4x, Modifier.weight(1f))
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SensiValueCard("Mira AWM", sensiConfig.miraAwm, Modifier.weight(1f))
          SensiValueCard("Olhadinha", sensiConfig.olhadinha, Modifier.weight(1f))
        }
      }
    }

    // Section "CONFIG. SENSI" exact to Screenshot 3
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CyberCard)
        .border(1.dp, Color(0xFF28283C), RoundedCornerShape(16.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF330910))
          .border(1.dp, NeonRed, RoundedCornerShape(12.dp))
          .padding(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Text(
          text = "CONFIG. SENSI",
          color = NeonRed,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      SensiToggleRow(
        title = "Sensi do Celular",
        subtitle = "Ajuste automático de sensibilidade",
        icon = Icons.Default.Speed,
        checked = sensiCelularActive,
        onCheckedChange = { sensiCelularActive = it },
        testTag = "toggle_sensi_celular"
      )

      SensiToggleRow(
        title = "HUD Mobile",
        subtitle = "Otimizador de layout de controle",
        icon = Icons.Default.GridOn,
        checked = hudMobileActive,
        onCheckedChange = { hudMobileActive = it },
        testTag = "toggle_hud_mobile"
      )

      SensiToggleRow(
        title = "DPI do Celular",
        subtitle = "Preset de escala de renderização",
        icon = Icons.Default.Tune,
        checked = dpiCelularActive,
        onCheckedChange = { dpiCelularActive = it },
        testTag = "toggle_dpi_celular"
      )

      SensiToggleRow(
        title = "Sensi da Arma",
        subtitle = "Perfis de sensibilidade por arma",
        icon = Icons.Default.Speed,
        checked = sensiArmaActive,
        onCheckedChange = { sensiArmaActive = it },
        testTag = "toggle_sensi_arma"
      )
    }

    // Section "MIRA" exact to Screenshot 3
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CyberCard)
        .border(1.dp, Color(0xFF28283C), RoundedCornerShape(16.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF330910))
          .border(1.dp, NeonRed, RoundedCornerShape(12.dp))
          .padding(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Text(
          text = "MIRA",
          color = NeonRed,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      SensiToggleRow(
        title = "Mira Básica",
        subtitle = "Pack de mira personalizada",
        icon = Icons.Default.Visibility,
        checked = miraBasicaActive,
        onCheckedChange = { miraBasicaActive = it },
        testTag = "toggle_mira_basica"
      )

      SensiToggleRow(
        title = "Config. Mira",
        subtitle = "Tamanho, espessura, gap, opacidade",
        icon = Icons.Default.Tune,
        checked = configMiraActive,
        onCheckedChange = { configMiraActive = it },
        testTag = "toggle_config_mira"
      )
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun SensiValueCard(
  name: String,
  value: Int,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(Color(0xFF141420))
      .border(1.dp, Color(0xFF2E2E42), RoundedCornerShape(10.dp))
      .padding(10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = name,
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = "$value",
        color = NeonRed,
        fontSize = 15.sp,
        fontWeight = FontWeight.Black
      )
    }
  }
}

@Composable
private fun SensiToggleRow(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  testTag: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      modifier = Modifier.weight(1f),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF1A1A24)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = NeonRed,
          modifier = Modifier.size(18.dp)
        )
      }

      Column {
        Text(
          text = title,
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = subtitle,
          color = TextMuted,
          fontSize = 11.sp
        )
      }
    }

    NeonSwitch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      testTag = testTag
    )
  }
}
