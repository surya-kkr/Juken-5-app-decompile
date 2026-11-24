package com.kkr.juken.domain.model.tuning

data class HistoryEntry(
    val coordinate: CellCoordinate,
    val minAfr: Double,
    val maxAfr: Double,
    val averageAfr: Double,
    val hitCount: Int
)
