package com.example.model

import androidx.compose.ui.graphics.Color

enum class CrosshairStyle(val displayName: String, val iconLabel: String) {
  DOT("Ponto", "•"),
  CIRCLE("Círculo", "☉"),
  CROSSHAIR("Cruz (+)", "+"),
  BOX_DOT("Caixa ([+])", "[+]")
}

data class PresetColor(
  val name: String,
  val hex: String,
  val color: Color
)

val AvailableColors = listOf(
  PresetColor("White", "#FFFFFF", Color(0xFFFFFFFF)),
  PresetColor("Red", "#FF0033", Color(0xFFFF0033)),
  PresetColor("Yellow", "#FFEA00", Color(0xFFFFEA00)),
  PresetColor("Green", "#00E676", Color(0xFF00E676)),
  PresetColor("Cyan", "#00E5FF", Color(0xFF00E5FF))
)

data class CrosshairConfig(
  val enabled: Boolean = true,
  val style: CrosshairStyle = CrosshairStyle.DOT,
  val colorHex: String = "#FF0033",
  val sizeDp: Float = 26f,
  val opacity: Float = 0.95f,
  val quickHide: Boolean = false,
  val overlayFloatingActive: Boolean = true,
  val floatingX: Float = 0f,
  val floatingY: Float = 0f
)

enum class WeaponCategory(
  val id: String,
  val title: String,
  val subtitle: String,
  val baseGeral: Int,
  val baseRedDot: Int,
  val base2x: Int,
  val base4x: Int,
  val baseAwm: Int
) {
  SMG("smg", "SMG (MP40, UMP, Thompson)", "Puxada rápida & alta cadência", 98, 97, 94, 91, 55),
  AR("ar", "AR (AK47, SCAR, M4A1)", "Precisão a média distância & controle de recuo", 94, 92, 88, 85, 60),
  SNIPER("sniper", "Sniper (AWM, Kar98k, M82B)", "Micro-ajuste estável & tiro rápido sem mira", 82, 80, 75, 70, 48),
  SHOTGUN("shotgun", "Shotgun (M1887, M1014)", "Flick verticais rápidos a curta distância", 100, 99, 95, 92, 50)
}

data class DevicePreset(
  val name: String,
  val recommendedDpi: Int,
  val refreshRate: Int
)

val CommonDevicePresets = listOf(
  DevicePreset("Samsung Galaxy M15 5G", 411, 90),
  DevicePreset("Xiaomi Poco X6 / F5", 480, 120),
  DevicePreset("Samsung Galaxy S23 / S24", 411, 120),
  DevicePreset("Samsung Galaxy A54 / A55", 411, 120),
  DevicePreset("iPhone 13 / 14 / 15", 460, 60),
  DevicePreset("iPhone 13 Pro / 14 Pro / 15 Pro", 460, 120),
  DevicePreset("Motorola Edge 40 / 50", 432, 144),
  DevicePreset("Asus ROG Phone / BlackShark", 600, 144),
  DevicePreset("Custom / Genérico", 411, 90)
)

data class SensitivityConfig(
  val screenDpi: Int = 480,
  val refreshRateHz: Int = 120,
  val fovAngle: Int = 90,
  val selectedWeapon: WeaponCategory = WeaponCategory.SMG,
  val selectedDevice: String = "Xiaomi Poco X6 / F5",
  val geral: Int = 98,
  val pontoVermelho: Int = 96,
  val mira2x: Int = 93,
  val mira4x: Int = 89,
  val miraAwm: Int = 56,
  val olhadinha: Int = 84
)

data class OptimizerState(
  val performanceMode: Boolean = true,
  val touchOptimization: Boolean = true,
  val precisionMode: Boolean = true,
  val protectionSystem: Boolean = true,
  val fpsBooster: Boolean = true,
  // Proxy Menu HS Toggles
  val hsAlto: Boolean = true,
  val hsAltoNeck: Boolean = false,
  val hsCaveira: Boolean = true,
  val hsNeckLegit: Boolean = false,
  // Live stats & RAM
  val totalRamMb: Int = 8192,
  val usedRamMb: Int = 5480,
  val freedRamMb: Int = 0,
  val currentFps: Int = 120,
  val touchLatencyMs: Float = 1.1f,
  val isBoosting: Boolean = false,
  val boostProgress: Float = 0f,
  val boostStatusText: String = "Sistema Otimizado • Latência 1.1ms"
)

data class LaunchSequenceState(
  val isLaunching: Boolean = false,
  val progress: Float = 0f,
  val currentMessage: String = "",
  val completed: Boolean = false
)
