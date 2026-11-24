package com.kkr.juken.domain.model.tuning

data class History(
    val entries: Map<CellCoordinate, HistoryEntry>
)
