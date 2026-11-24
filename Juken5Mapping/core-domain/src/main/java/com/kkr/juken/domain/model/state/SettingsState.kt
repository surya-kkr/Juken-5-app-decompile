package com.kkr.juken.domain.model.state

enum class AppTheme { LIGHT, DARK, OLED, SYSTEM }
enum class TransportType { BLUETOOTH, SIMULATOR, USB, WIFI }
enum class UnitSystem { METRIC, IMPERIAL }

data class SettingsState(
    val theme: AppTheme = AppTheme.DARK,
    val isDeveloperModeEnabled: Boolean = false,
    val autoConnect: Boolean = true,
    val selectedTransport: TransportType = TransportType.SIMULATOR,
    val language: String = "en",
    val units: UnitSystem = UnitSystem.METRIC,
    val isLoggingEnabled: Boolean = false
)
