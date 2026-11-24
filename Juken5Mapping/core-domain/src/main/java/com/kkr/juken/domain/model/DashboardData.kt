package com.kkr.juken.domain.model

data class DashboardData(
    val rpm: Int = 0,
    val tps: Float = 0f,
    val afr: Float = 0f,
    val batteryVoltage: Float = 0f,
    val engineTemp: Int = 0
)
