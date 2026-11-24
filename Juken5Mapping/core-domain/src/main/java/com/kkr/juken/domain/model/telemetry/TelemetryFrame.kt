package com.kkr.juken.domain.model.telemetry

/**
 * High-performance modular telemetry frame.
 * Composed of sub-states to allow for future expansion without breaking binary compatibility.
 */
data class TelemetryFrame(
    val timestamp: Long,
    val engine: EngineState,
    val electrical: ElectricalState,
    val fuel: FuelState,
    val ignition: IgnitionState,
    val environment: EnvironmentState,
    val connection: ConnectionStats,
    val metadata: FrameMetadata
)

data class EngineState(
    val rpm: Int,
    val tps: Double,
    val manifoldPressure: Double,
    val speed: Double = 0.0,
    val gear: Int = 0
)

data class ElectricalState(
    val batteryVoltage: Double,
    val primaryVoltage: Double = 0.0
)

data class FuelState(
    val afr: Double,
    val lambda: Double,
    val targetAfr: Double = 0.0,
    val injectorDuty: Double,
    val fuelCorrection: Double = 0.0
)

data class IgnitionState(
    val timingAdvance: Double,
    val dwellTime: Double = 0.0,
    val knockDetected: Boolean = false
)

data class EnvironmentState(
    val coolantTemp: Int,
    val airTemp: Int = 0,
    val baroPressure: Double = 0.0
)

data class ConnectionStats(
    val latencyMs: Int,
    val packetLoss: Float = 0f,
    val reconnectCount: Int = 0
)

data class FrameMetadata(
    val activeCellIndex: Int? = null,
    val interpolationIndices: List<Int> = emptyList(),
    val activeMapType: String = "FUEL"
)
