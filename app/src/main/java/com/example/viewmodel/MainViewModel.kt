package com.example.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AvailableColors
import com.example.model.CommonDevicePresets
import com.example.model.CrosshairConfig
import com.example.model.CrosshairStyle
import com.example.model.LaunchSequenceState
import com.example.model.OptimizerState
import com.example.model.SensitivityConfig
import com.example.model.WeaponCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val _crosshairConfig = MutableStateFlow(CrosshairConfig())
  val crosshairConfig: StateFlow<CrosshairConfig> = _crosshairConfig.asStateFlow()

  private val _sensitivityConfig = MutableStateFlow(SensitivityConfig())
  val sensitivityConfig: StateFlow<SensitivityConfig> = _sensitivityConfig.asStateFlow()

  private val _optimizerState = MutableStateFlow(OptimizerState())
  val optimizerState: StateFlow<OptimizerState> = _optimizerState.asStateFlow()

  private val _launchState = MutableStateFlow(LaunchSequenceState())
  val launchState: StateFlow<LaunchSequenceState> = _launchState.asStateFlow()

  // Target Range Stats
  private val _hitsCount = MutableStateFlow(0)
  val hitsCount: StateFlow<Int> = _hitsCount.asStateFlow()

  private val _headshotsCount = MutableStateFlow(0)
  val headshotsCount: StateFlow<Int> = _headshotsCount.asStateFlow()

  init {
    recalculateSensitivity()
  }

  // Crosshair controls
  fun setCrosshairStyle(style: CrosshairStyle) {
    _crosshairConfig.update { it.copy(style = style) }
    vibrateShort()
  }

  fun setCrosshairColor(hex: String) {
    _crosshairConfig.update { it.copy(colorHex = hex) }
    vibrateShort()
  }

  fun setCrosshairSize(size: Float) {
    _crosshairConfig.update { it.copy(sizeDp = size) }
  }

  fun setCrosshairOpacity(opacity: Float) {
    _crosshairConfig.update { it.copy(opacity = opacity) }
  }

  fun toggleCrosshairEnabled(enabled: Boolean) {
    _crosshairConfig.update { it.copy(enabled = enabled) }
    vibrateShort()
  }

  fun toggleQuickHide(quickHide: Boolean) {
    _crosshairConfig.update { it.copy(quickHide = quickHide) }
    vibrateShort()
  }

  fun updateFloatingPosition(dx: Float, dy: Float) {
    _crosshairConfig.update {
      it.copy(
        floatingX = (it.floatingX + dx).coerceIn(-400f, 400f),
        floatingY = (it.floatingY + dy).coerceIn(-600f, 600f)
      )
    }
  }

  fun resetFloatingPosition() {
    _crosshairConfig.update { it.copy(floatingX = 0f, floatingY = 0f) }
  }

  // Sensitivity calculation engine
  fun setScreenDpi(dpi: Int) {
    _sensitivityConfig.update { it.copy(screenDpi = dpi) }
    recalculateSensitivity()
  }

  fun setRefreshRate(hz: Int) {
    _sensitivityConfig.update { it.copy(refreshRateHz = hz) }
    recalculateSensitivity()
    vibrateShort()
  }

  fun setFovAngle(angle: Int) {
    _sensitivityConfig.update { it.copy(fovAngle = angle) }
    recalculateSensitivity()
  }

  fun setWeaponCategory(weapon: WeaponCategory) {
    _sensitivityConfig.update { it.copy(selectedWeapon = weapon) }
    recalculateSensitivity()
    vibrateShort()
  }

  fun applyDevicePreset(devicePresetName: String) {
    val found = CommonDevicePresets.find { it.name == devicePresetName }
    if (found != null) {
      _sensitivityConfig.update {
        it.copy(
          selectedDevice = found.name,
          screenDpi = found.recommendedDpi,
          refreshRateHz = found.refreshRate
        )
      }
      recalculateSensitivity()
      vibrateShort()
    }
  }

  private fun recalculateSensitivity() {
    val cfg = _sensitivityConfig.value
    val dpiFactor = (480.0 / cfg.screenDpi.toDouble()).coerceIn(0.7, 1.35)
    val refreshFactor = when (cfg.refreshRateHz) {
      60 -> 1.05
      90 -> 1.02
      120 -> 0.98
      144 -> 0.95
      else -> 1.0
    }
    val fovFactor = (cfg.fovAngle / 90.0).coerceIn(0.85, 1.15)
    val weapon = cfg.selectedWeapon

    val geralVal = ((weapon.baseGeral * dpiFactor * refreshFactor * fovFactor).toInt()).coerceIn(75, 100)
    val redDotVal = ((weapon.baseRedDot * dpiFactor * refreshFactor * fovFactor).toInt()).coerceIn(70, 100)
    val mira2xVal = ((weapon.base2x * dpiFactor * refreshFactor).toInt()).coerceIn(65, 100)
    val mira4xVal = ((weapon.base4x * dpiFactor * refreshFactor).toInt()).coerceIn(60, 100)
    val miraAwmVal = ((weapon.baseAwm * dpiFactor * 0.9).toInt()).coerceIn(35, 90)
    val olhadinhaVal = ((85 * dpiFactor * refreshFactor).toInt()).coerceIn(60, 100)

    _sensitivityConfig.update {
      it.copy(
        geral = geralVal,
        pontoVermelho = redDotVal,
        mira2x = mira2xVal,
        mira4x = mira4xVal,
        miraAwm = miraAwmVal,
        olhadinha = olhadinhaVal
      )
    }
  }

  fun copySensitivityToClipboard() {
    val cfg = _sensitivityConfig.value
    val text = """
      🎯 SENSI PENHAYZZ VIP OTIMIZADA:
      📱 Dispositivo: ${cfg.selectedDevice}
      📐 DPI: ${cfg.screenDpi} | Taxa: ${cfg.refreshRateHz}Hz | FOV: ${cfg.fovAngle}°
      🔫 Perfil: ${cfg.selectedWeapon.title}
      ━━━━━━━━━━━━━━━━━━━
      • Geral: ${cfg.geral}
      • Ponto Vermelho: ${cfg.pontoVermelho}
      • Mira 2x: ${cfg.mira2x}
      • Mira 4x: ${cfg.mira4x}
      • Mira AWM: ${cfg.miraAwm}
      • Olhadinha: ${cfg.olhadinha}
      ━━━━━━━━━━━━━━━━━━━
      Otimizado com Sucesso!
    """.trimIndent()

    val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText("Config Sensi Free Fire", text)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(getApplication(), "Configuração copiada para a área de transferência!", Toast.LENGTH_SHORT).show()
    vibrateShort()
  }

  // Optimizer & Proxy Switches
  fun togglePerformanceMode(active: Boolean) {
    _optimizerState.update { it.copy(performanceMode = active) }
    vibrateShort()
  }

  fun toggleTouchOptimization(active: Boolean) {
    _optimizerState.update {
      it.copy(
        touchOptimization = active,
        touchLatencyMs = if (active) 1.0f else 3.8f
      )
    }
    vibrateShort()
  }

  fun togglePrecisionMode(active: Boolean) {
    _optimizerState.update { it.copy(precisionMode = active) }
    vibrateShort()
  }

  fun toggleProtectionSystem(active: Boolean) {
    _optimizerState.update { it.copy(protectionSystem = active) }
    vibrateShort()
  }

  fun toggleFpsBooster(active: Boolean) {
    _optimizerState.update {
      it.copy(
        fpsBooster = active,
        currentFps = if (active) 120 else 60
      )
    }
    vibrateShort()
  }

  // Proxy Menu HS Switches
  fun toggleHsAlto(active: Boolean) {
    _optimizerState.update { it.copy(hsAlto = active) }
    vibrateShort()
  }

  fun toggleHsAltoNeck(active: Boolean) {
    _optimizerState.update { it.copy(hsAltoNeck = active) }
    vibrateShort()
  }

  fun toggleHsCaveira(active: Boolean) {
    _optimizerState.update { it.copy(hsCaveira = active) }
    vibrateShort()
  }

  fun toggleHsNeckLegit(active: Boolean) {
    _optimizerState.update { it.copy(hsNeckLegit = active) }
    vibrateShort()
  }

  // RAM Booster Simulation
  fun triggerRamBooster() {
    if (_optimizerState.value.isBoosting) return
    viewModelScope.launch {
      _optimizerState.update {
        it.copy(
          isBoosting = true,
          boostProgress = 0.1f,
          boostStatusText = "Identificando processos em segundo plano..."
        )
      }
      delay(400)
      _optimizerState.update {
        it.copy(
          boostProgress = 0.35f,
          boostStatusText = "Otimizando buffer de amostragem de toque..."
        )
      }
      delay(450)
      _optimizerState.update {
        it.copy(
          boostProgress = 0.70f,
          boostStatusText = "Limpando caches e desalocando texturas..."
        )
      }
      delay(450)
      val freed = (400..550).random()
      val newUsed = (_optimizerState.value.usedRamMb - freed).coerceAtLeast(3200)
      _optimizerState.update {
        it.copy(
          isBoosting = false,
          boostProgress = 1.0f,
          freedRamMb = freed,
          usedRamMb = newUsed,
          currentFps = 120,
          touchLatencyMs = 0.9f,
          boostStatusText = "Liberados ${freed}MB de RAM! Dispositivo acelerado ao máximo!"
        )
      }
      vibrateBoostComplete()
    }
  }

  // Launch Free Fire sequence
  fun launchFreeFire() {
    if (_launchState.value.isLaunching) return
    viewModelScope.launch {
      _launchState.value = LaunchSequenceState(
        isLaunching = true,
        progress = 0.15f,
        currentMessage = "Aplicando Sensibilidade e Escala de DPI..."
      )
      delay(500)
      _launchState.value = LaunchSequenceState(
        isLaunching = true,
        progress = 0.45f,
        currentMessage = "Ativando Overlay de Mira & Estabilização..."
      )
      delay(500)
      _launchState.value = LaunchSequenceState(
        isLaunching = true,
        progress = 0.80f,
        currentMessage = "Acelerando FPS para 120Hz & Otimizando Toque..."
      )
      delay(500)
      _launchState.value = LaunchSequenceState(
        isLaunching = true,
        progress = 1.0f,
        currentMessage = "Tudo pronto! Iniciando Free Fire...",
        completed = true
      )
      vibrateBoostComplete()
      delay(400)

      // Attempt to launch Free Fire or Free Fire MAX via package intent
      val context = getApplication<Application>()
      val ffMaxIntent = context.packageManager.getLaunchIntentForPackage("com.dts.freefiremax")
      val ffStandardIntent = context.packageManager.getLaunchIntentForPackage("com.dts.freefireth")

      val targetIntent = ffMaxIntent ?: ffStandardIntent
      if (targetIntent != null) {
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(targetIntent)
      } else {
        // Show informative toast and offer store link
        Toast.makeText(
          context,
          "Configurações aplicadas com Sucesso! Mira e Sensi prontas.",
          Toast.LENGTH_LONG
        ).show()
      }
      delay(1200)
      _launchState.value = LaunchSequenceState(isLaunching = false)
    }
  }

  fun registerTargetHit(isHeadshot: Boolean) {
    _hitsCount.update { it + 1 }
    if (isHeadshot) {
      _headshotsCount.update { it + 1 }
      vibrateHeadshot()
    } else {
      vibrateShort()
    }
  }

  fun resetTargetStats() {
    _hitsCount.value = 0
    _headshotsCount.value = 0
  }

  private fun vibrateShort() {
    try {
      val context = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(20)
      }
    } catch (_: Exception) {}
  }

  private fun vibrateHeadshot() {
    try {
      val context = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        val pattern = longArrayOf(0, 30, 40, 60)
        val amplitudes = intArrayOf(0, 180, 0, 255)
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createWaveform(pattern, amplitudes, -1)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(longArrayOf(0, 30, 40, 60), -1)
      }
    } catch (_: Exception) {}
  }

  private fun vibrateBoostComplete() {
    try {
      val context = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(50)
      }
    } catch (_: Exception) {}
  }
}
