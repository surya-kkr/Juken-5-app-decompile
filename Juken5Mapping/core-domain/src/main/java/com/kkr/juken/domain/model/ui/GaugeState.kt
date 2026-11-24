package com.kkr.juken.domain.model.ui

/**
 * Common state for a single UI gauge.
 */
data class GaugeState(
    val value: Float = 0f,
    val min: Float = 0f,
    val max: Float = 100f,
    val warningThreshold: Float? = null,
    val criticalThreshold: Float? = null,
    val unit: String = "",
    val label: String = ""
)
