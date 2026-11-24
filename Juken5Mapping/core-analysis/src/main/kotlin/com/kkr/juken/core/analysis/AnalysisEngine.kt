package com.kkr.juken.core.analysis

import com.kkr.juken.domain.model.telemetry.TelemetryFrame

/**
 * High-performance analysis engine for detecting engine states and driver intents.
 */
class AnalysisEngine {

    enum class DrivingMode { 
        IDLE, CRUISE, ACCELERATING, DECELERATING, WOT, COLD_START 
    }

    /**
     * Classifies a frame into a driving mode based on sensor heuristics.
     */
    fun classifyMode(frame: TelemetryFrame): DrivingMode {
        return when {
            frame.engine.tps > 90.0 -> DrivingMode.WOT
            frame.engine.rpm < 1200 && frame.engine.tps < 2.0 -> DrivingMode.IDLE
            frame.environment.coolantTemp < 40 -> DrivingMode.COLD_START
            // In a production scenario, we would calculate Delta TPS/RPM over a window
            else -> DrivingMode.CRUISE
        }
    }

    /**
     * Calculates the error percentage compared to a target AFR.
     */
    fun calculateAfrError(measured: Double, target: Double): Double {
        if (target <= 0.0) return 0.0
        return (measured - target) / target
    }
}
