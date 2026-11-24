package com.kkr.juken.domain.model.engine

data class LiveData(
    val rpm: Int,
    val tps: Float,
    val afr: Float,
    val batteryVoltage: Float,
    val engineTemp: Int,
    val timestamp: Long = System.currentTimeMillis()
) {
    init {
        require(rpm >= 0) { "RPM cannot be negative" }
        require(tps in 0f..100f) { "TPS must be between 0 and 100" }
    }
}
