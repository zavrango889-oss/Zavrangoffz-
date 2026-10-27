package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AimAssistScreen
import com.example.ui.screens.ConfigSensiScreen
import com.example.ui.screens.OptimizerScreen
import com.example.ui.screens.ProxyMenuScreen
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

enum class NavTab(
  val title: String,
  val icon: ImageVector,
  val tag: String
) {
  AIM_ASSIST("Mira HUD", Icons.Default.GpsFixed, "tab_aim_assist"),
  CONFIG_SENSI("Sensi", Icons.Default.Tune, "tab_config_sensi"),
  OPTIMIZER("Booster", Icons.Default.Bolt, "tab_optimizer"),
  PROXY_MENU("Proxy HS", Icons.Default.Security, "tab_proxy_menu")
}

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack),
          containerColor = CyberBlack,
          bottomBar = {
            BottomGamingNavBar(
              selectedTabIndex = selectedTabIndex,
              onTabSelected = { selectedTabIndex = it },
              modifier = Modifier.navigationBarsPadding()
            )
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .statusBarsPadding()
          ) {
            when (selectedTabIndex) {
              0 -> AimAssistScreen(viewModel = viewModel)
              1 -> ConfigSensiScreen(viewModel = viewModel)
              2 -> OptimizerScreen(viewModel = viewModel)
              3 -> ProxyMenuScreen(viewModel = viewModel)
            }
          }
        }
      }
    }
  }
}

@Composable
fun BottomGamingNavBar(
  selectedTabIndex: Int,
  onTabSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .shadow(16.dp, RoundedCornerShape(22.dp), ambientColor = NeonRed, spotColor = Color.Black)
      .clip(RoundedCornerShape(22.dp))
      .background(
        Brush.verticalGradient(
          listOf(Color(0xF5181826), Color(0xF8101018), Color(0xFA09090E))
        )
      )
      .border(
        width = 1.2.dp,
        brush = Brush.horizontalGradient(
          listOf(Color(0x44FF0033), Color(0x3300E5FF), Color(0x44FF0033))
        ),
        shape = RoundedCornerShape(22.dp)
      )
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavTab.entries.forEachIndexed { index, tab ->
        val isSelected = selectedTabIndex == index
        Box(
          modifier = Modifier
            .weight(1f)
            .minimumInteractiveComponentSize()
            .testTag(tab.tag)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFF330812) else Color.Transparent)
            .border(
              width = if (isSelected) 1.dp else 0.dp,
              color = if (isSelected) NeonRed else Color.Transparent,
              shape = RoundedCornerShape(16.dp)
            )
            .clickable { onTabSelected(index) }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Icon(
              imageVector = tab.icon,
              contentDescription = tab.title,
              tint = if (isSelected) NeonRed else TextMuted,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = tab.title,
              color = if (isSelected) TextPrimary else TextMuted,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
            )
          }
        }
      }
    }
  }
}
