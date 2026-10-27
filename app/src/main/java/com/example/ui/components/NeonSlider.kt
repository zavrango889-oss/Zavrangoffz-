package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeonSlider(
  label: String,
  value: Float,
  onValueChange: (Float) -> Unit,
  valueRange: ClosedFloatingPointRange<Float>,
  valueText: String,
  modifier: Modifier = Modifier,
  testTag: String = "neon_slider",
  accentColor: Color = NeonRed
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = label,
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = valueText,
        color = accentColor,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Slider(
      value = value,
      onValueChange = onValueChange,
      valueRange = valueRange,
      colors = SliderDefaults.colors(
        thumbColor = Color.White,
        activeTrackColor = accentColor,
        inactiveTrackColor = Color(0xFF282836)
      ),
      thumb = {
        Box(
          modifier = Modifier
            .size(18.dp)
            .shadow(6.dp, CircleShape, ambientColor = accentColor, spotColor = accentColor)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, accentColor, CircleShape)
        )
      },
      track = { sliderState ->
        SliderDefaults.Track(
          sliderState = sliderState,
          colors = SliderDefaults.colors(
            activeTrackColor = accentColor,
            inactiveTrackColor = Color(0xFF242433)
          ),
          modifier = Modifier.height(4.dp)
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 0.dp)
    )
  }
}
