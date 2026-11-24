package com.kkr.juken.domain.model.ui

import com.kkr.juken.domain.model.tuning.MapType

/**
 * UI representation of a tuning map.
 */
data class MapState(
    val type: MapType,
    val isDirty: Boolean = false,
    val lastSyncTime: Long = 0,
    val cells: List<Float> = emptyList(),
    val rows: Int = 21,
    val cols: Int = 61
)
