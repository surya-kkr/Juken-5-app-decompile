package com.kkr.juken.domain.repository

import com.kkr.juken.domain.model.tuning.MapType
import com.kkr.juken.domain.model.tuning.TuningMap
import kotlinx.coroutines.flow.StateFlow

/**
 * Manages the active calibration state and domain-level map arithmetic.
 */
interface CalibrationRepository {
    val activeCalibration: StateFlow<Map<MapType, TuningMap>>
    
    suspend fun updateCell(type: MapType, index: Int, value: Int)
    suspend fun applyDelta(type: MapType, index: Int, delta: Int)
}
