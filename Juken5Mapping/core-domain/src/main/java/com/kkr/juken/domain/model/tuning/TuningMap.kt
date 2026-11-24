package com.kkr.juken.domain.model.tuning

import com.kkr.juken.domain.model.tuning.MapType

data class TuningMap(
    val type: MapType,
    val coreId: Int,
    val rows: Int,
    val cols: Int,
    val cells: List<TuningCell>
) {
    init {
        require(cells.size == rows * cols) { "Cell count mismatch: expected ${rows * cols}, got ${cells.size}" }
    }

    fun getCell(row: Int, col: Int): TuningCell {
        return cells[row * cols + col]
    }
}
