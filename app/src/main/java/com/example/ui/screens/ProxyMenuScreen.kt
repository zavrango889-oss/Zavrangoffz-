package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.ui.components.NeonSwitch
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@Composable
fun ProxyMenuScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val optimizerState by viewModel.optimizerState.collectAsStateWithLifecycle()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header matching Screenshot 2: "PROXY MENU" & "● Online"
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF22070D))
            .border(1.dp, NeonRed, RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu Proxy",
            tint = NeonRed,
            modifier = Modifier.size(20.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "PROXY ",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black
          )
          Text(
            text = "MENU",
            color = NeonRed,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF00E676))
        )
        Text(
          text = "Online",
          color = NeonRed,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // Main Proxy Card matching Screenshot 2
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(16.dp, RoundedCornerShape(18.dp), ambientColor = NeonRed, spotColor = NeonRed)
        .clip(RoundedCornerShape(18.dp))
        .background(
          Brush.verticalGradient(
            listOf(Color(0xFF14080D), Color(0xFF0F060A), Color(0xFF08080C))
          )
        )
        .border(
          width = 1.5.dp,
          brush = Brush.verticalGradient(
            listOf(NeonRed, Color(0x66FF0033), Color(0x22FFFFFF))
          ),
          shape = RoundedCornerShape(18.dp)
        )
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Section Title: "FUNCOES"
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .width(4.dp)
              .height(16.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(NeonRed)
          )
          Text(
            text = "FUNÇÕES",
            color = NeonRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
          )
        }

        // HS Alto
        ProxyFunctionRow(
          title = "HS Alto",
          subtitle = "HS Acima da Cabeça",
          checked = optimizerState.hsAlto,
          onCheckedChange = { viewModel.toggleHsAlto(it) },
          testTag = "toggle_hs_alto"
        )

        HorizontalDivider(color = Color(0xFF24151C), thickness = 0.8.dp)

        // HS Alto + Neck
        ProxyFunctionRow(
          title = "HS Alto + Neck",
          subtitle = "HS Alto com Pescoço",
          checked = optimizerState.hsAltoNeck,
          onCheckedChange = { viewModel.toggleHsAltoNeck(it) },
          testTag = "toggle_hs_alto_neck"
        )

        HorizontalDivider(color = Color(0xFF24151C), thickness = 0.8.dp)

        // HS Caveira
        ProxyFunctionRow(
          title = "HS Caveira",
          subtitle = "HS Abaixo do Pescoço",
          checked = optimizerState.hsCaveira,
          onCheckedChange = { viewModel.toggleHsCaveira(it) },
          testTag = "toggle_hs_caveira"
        )

        HorizontalDivider(color = Color(0xFF24151C), thickness = 0.8.dp)

        // HS Neck
        ProxyFunctionRow(
          title = "HS Neck",
          subtitle = "HS Pescoço Legit",
          checked = optimizerState.hsNeckLegit,
          onCheckedChange = { viewModel.toggleHsNeckLegit(it) },
          testTag = "toggle_hs_neck"
        )
      }
    }

    // Safety and Anti-Blacklist Badges matching Screenshot 5
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
        text = "SISTEMA DE SEGURANÇA & BYPASS LEGIT",
        color = NeonCyan,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        SafetyBadgeCard(
          icon = Icons.Default.Shield,
          title = "100% SEGURO",
          subtitle = "É ANTI BLACKLIST",
          modifier = Modifier.weight(1f)
        )
        SafetyBadgeCard(
          icon = Icons.Default.Bolt,
          title = "INJEÇÃO RÁPIDA",
          subtitle = "E SEM ERROS",
          modifier = Modifier.weight(1f)
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        SafetyBadgeCard(
          icon = Icons.Default.Settings,
          title = "COMPATÍVEL",
          subtitle = "COM TODOS OS ANDROID",
          modifier = Modifier.weight(1f)
        )
        SafetyBadgeCard(
          icon = Icons.Default.CheckCircle,
          title = "SEM BANIMENTO",
          subtitle = "E SEM RISCO",
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Terminal Status Console
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFF090A10))
        .border(1.dp, Color(0xFF1E2235), RoundedCornerShape(14.dp))
        .padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Text(
        text = "CONSOLE DE STATUS PROXY",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = "> [LOG 04:14:52] Kernel touch hook: OK (latência 0.9ms)\n" +
               "> [LOG 04:14:55] DPI Virtual Scale: 480 (padrão competitivo)\n" +
               "> [LOG 04:15:00] Bypass anti-cheat checks: Verificado\n" +
               "> [STATUS] Pronto para Free Fire & Free Fire MAX",
        color = NeonCyan,
        fontSize = 10.sp,
        lineHeight = 15.sp,
        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
      )
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun ProxyFunctionRow(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  testTag: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = title,
        color = TextPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 12.sp
      )
    }

    NeonSwitch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      testTag = testTag
    )
  }
}

@Composable
private fun SafetyBadgeCard(
  icon: ImageVector,
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF13131D))
      .border(1.dp, Color(0xFF331520), RoundedCornerShape(12.dp))
      .padding(10.dp)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = NeonRed,
        modifier = Modifier.size(22.dp)
      )
      Text(
        text = title,
        color = TextPrimary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 9.sp
      )
    }
  }
}
