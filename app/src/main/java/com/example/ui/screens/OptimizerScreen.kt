package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.LaunchSequenceState
import com.example.model.OptimizerState
import com.example.ui.components.NeonSwitch
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedDark
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@Composable
fun OptimizerScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val optimizerState by viewModel.optimizerState.collectAsStateWithLifecycle()
  val launchState by viewModel.launchState.collectAsStateWithLifecycle()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top VIP Profile Bar matching Screenshot 4
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Red glowing profile avatar
        Box(
          modifier = Modifier
            .size(46.dp)
            .shadow(10.dp, CircleShape, ambientColor = NeonRed, spotColor = NeonRed)
            .clip(CircleShape)
            .background(Color(0xFF28070F))
            .border(1.5.dp, NeonRed, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_game_mascot),
            contentDescription = "Avatar VIP Zavrangoffz",
            modifier = Modifier.size(40.dp).clip(CircleShape)
          )
        }

        Column {
          Text(
            text = "Zavrangoffz",
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "VIP",
            color = NeonRed,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black
          )
        }
      }

      Text(
        text = "Acesso permanente",
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )
    }

    // Hardware Telemetry & RAM Booster Card
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CyberCard)
        .border(1.dp, Color(0xFF28283C), RoundedCornerShape(16.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "TELEMETRIA EM TEMPO REAL",
          color = NeonCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(Color(0xFF00E676))
          )
          Text(
            text = "Estável",
            color = Color(0xFF00E676),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Three metrics
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val ramPercent = ((optimizerState.usedRamMb.toFloat() / optimizerState.totalRamMb) * 100).toInt()
        MetricTile(
          label = "RAM Usada",
          value = "$ramPercent%",
          sub = "${optimizerState.usedRamMb}MB / ${optimizerState.totalRamMb}MB",
          color = if (ramPercent > 75) NeonYellow else NeonCyan,
          modifier = Modifier.weight(1f)
        )

        MetricTile(
          label = "Taxa de Quadros",
          value = "${optimizerState.currentFps} FPS",
          sub = "Taxa Estável",
          color = Color(0xFF00E676),
          modifier = Modifier.weight(1f)
        )

        MetricTile(
          label = "Latência Toque",
          value = "${optimizerState.touchLatencyMs} ms",
          sub = "Buffer Otimizado",
          color = NeonRed,
          modifier = Modifier.weight(1f)
        )
      }

      // RAM Progress Gauge
      val animatedRamFraction by animateFloatAsState(
        targetValue = optimizerState.usedRamMb.toFloat() / optimizerState.totalRamMb,
        animationSpec = tween(500),
        label = "ram_fraction"
      )

      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LinearProgressIndicator(
          progress = { animatedRamFraction },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = if (optimizerState.isBoosting) NeonYellow else NeonRed,
          trackColor = Color(0xFF232333)
        )

        Text(
          text = optimizerState.boostStatusText,
          color = if (optimizerState.freedRamMb > 0) Color(0xFF00E676) else TextMuted,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }

      // Boost Button
      Button(
        onClick = { viewModel.triggerRamBooster() },
        enabled = !optimizerState.isBoosting,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF261018),
          contentColor = NeonRed
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .border(1.2.dp, NeonRed.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
          .testTag("boost_ram_button")
      ) {
        if (optimizerState.isBoosting) {
          CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            color = NeonRed,
            strokeWidth = 2.dp
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Otimizando Sistema...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        } else {
          Icon(
            imageVector = Icons.Default.CleaningServices,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = NeonRed
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "LIMPAR RAM & ACELERAR FPS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }

    // List of 5 Feature Switches exact to Screenshot 4
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OptimizerOptionCard(
        title = "Performance Mode",
        description = "Melhora estabilidade e desempenho do dispositivo",
        icon = Icons.Default.Bolt,
        checked = optimizerState.performanceMode,
        onCheckedChange = { viewModel.togglePerformanceMode(it) },
        testTag = "toggle_performance_mode"
      )

      OptimizerOptionCard(
        title = "Touch Optimization",
        description = "Otimização de resposta do toque na tela",
        icon = Icons.Default.Fingerprint,
        checked = optimizerState.touchOptimization,
        onCheckedChange = { viewModel.toggleTouchOptimization(it) },
        testTag = "toggle_touch_optimization"
      )

      OptimizerOptionCard(
        title = "Precision Mode",
        description = "Ajustes avançados de controle e precisão",
        icon = Icons.Default.Adjust,
        checked = optimizerState.precisionMode,
        onCheckedChange = { viewModel.togglePrecisionMode(it) },
        testTag = "toggle_precision_mode"
      )

      OptimizerOptionCard(
        title = "Protection System",
        description = "Sistema de proteção e estabilidade",
        icon = Icons.Default.Shield,
        checked = optimizerState.protectionSystem,
        onCheckedChange = { viewModel.toggleProtectionSystem(it) },
        testTag = "toggle_protection_system"
      )

      OptimizerOptionCard(
        title = "FPS Booster",
        description = "Otimização para maior taxa de quadros",
        icon = Icons.Default.RocketLaunch,
        checked = optimizerState.fpsBooster,
        onCheckedChange = { viewModel.toggleFpsBooster(it) },
        testTag = "toggle_fps_booster"
      )
    }

    // The Big Glowing Neon Red Launch Button exact to Screenshot 4
    Button(
      onClick = { viewModel.launchFreeFire() },
      modifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .shadow(16.dp, RoundedCornerShape(16.dp), ambientColor = NeonRed, spotColor = NeonRed)
        .testTag("launch_free_fire_button"),
      colors = ButtonDefaults.buttonColors(
        containerColor = NeonRed,
        contentColor = Color.White
      ),
      shape = RoundedCornerShape(16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.PlayArrow,
          contentDescription = null,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "ABRIR FREE FIRE",
          fontSize = 16.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 1.2.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))
  }

  // Launch Sequence Progress Modal
  if (launchState.isLaunching) {
    AlertDialog(
      onDismissRequest = {},
      containerColor = Color(0xFF14141E),
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = null,
            tint = NeonRed,
            modifier = Modifier.size(22.dp)
          )
          Text(
            text = "INICIALIZANDO FREE FIRE",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
        }
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = launchState.currentMessage,
            color = TextSecondary,
            fontSize = 13.sp
          )

          LinearProgressIndicator(
            progress = { launchState.progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = NeonRed,
            trackColor = Color(0xFF28283C)
          )

          Text(
            text = "Hook do overlay ativado • FPS travado em 120Hz",
            color = TextMuted,
            fontSize = 11.sp
          )
        }
      },
      confirmButton = {}
    )
  }
}

@Composable
private fun MetricTile(
  label: String,
  value: String,
  sub: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(Color(0xFF141420))
      .border(1.dp, Color(0xFF282838), RoundedCornerShape(10.dp))
      .padding(8.dp)
  ) {
    Column {
      Text(text = label, color = TextMuted, fontSize = 10.sp)
      Text(text = value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
      Text(text = sub, color = TextSecondary, fontSize = 9.sp)
    }
  }
}

@Composable
private fun OptimizerOptionCard(
  title: String,
  description: String,
  icon: ImageVector,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  testTag: String
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0xFF14141E))
      .border(
        width = 1.dp,
        color = if (checked) Color(0x55FF0033) else Color(0xFF242436),
        shape = RoundedCornerShape(16.dp)
      )
      .padding(horizontal = 14.dp, vertical = 14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Red glow icon box exact to screenshot 4
        Box(
          modifier = Modifier
            .size(46.dp)
            .shadow(
              elevation = if (checked) 8.dp else 2.dp,
              shape = RoundedCornerShape(12.dp),
              ambientColor = NeonRed,
              spotColor = NeonRed
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF220A12))
            .border(
              1.dp,
              if (checked) NeonRed.copy(alpha = 0.8f) else Color(0xFF3A121E),
              RoundedCornerShape(12.dp)
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = NeonRed,
            modifier = Modifier.size(22.dp)
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = title,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = description,
            color = TextMuted,
            fontSize = 11.sp,
            lineHeight = 15.sp
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
}
