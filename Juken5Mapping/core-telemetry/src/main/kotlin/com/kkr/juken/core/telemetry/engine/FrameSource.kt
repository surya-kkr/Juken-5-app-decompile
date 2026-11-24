package com.kkr.juken.core.telemetry.engine

import com.kkr.juken.domain.model.telemetry.TelemetryFrame
import kotlinx.coroutines.flow.Flow

/**
 * Abstract source of telemetry data.
 * Decouples engines from specific I/O implementations.
 */
interface FrameSource {
    /**
     * Observable stream of telemetry frames.
     */
    val frames: Flow<TelemetryFrame>
    
    /**
     * Human-readable name of the source (e.g., "Live ECU", "Session_123.j5s").
     */
    val sourceName: String
}
