package com.kkr.juken.domain.model.ui

/**
 * Real-time engine variables for UI display.
 */
data class LiveState(
    val rpm: GaugeState = GaugeState(label = "RPM", max = 16000f, unit = "rpm", warningThreshold = 12000f, criticalThreshold = 14000f),
    val tps: GaugeState = GaugeState(label = "TPS", max = 100f, unit = "%"),
    val battery: GaugeState = GaugeState(label = "Battery", min = 8f, max = 16f, unit = "V", warningThreshold = 11.5f, criticalThreshold = 10.5f),
    val temperature: GaugeState = GaugeState(label = "Temp", min = 0f, max = 150f, unit = "°C", warningThreshold = 105f, criticalThreshold = 120f),
    val map: GaugeState = GaugeState(label = "MAP", max = 300f, unit = "kPa"),
    val afr: GaugeState = GaugeState(label = "AFR", min = 10f, max = 20f, unit = ":1")
)
