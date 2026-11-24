package com.kkr.juken.domain.model

data class TuningMap(
    val type: MapType,
    val coreId: Int = 1,
    val rows: Int = 21,
    val cols: Int = 61,
    val cells: List<Double> = List(rows * cols) { 0.0 }
) {
    fun getCell(row: Int, col: Int): Double {
        return cells[row * cols + col]
    }

    fun copyWithUpdate(row: Int, col: Int, value: Double): TuningMap {
        val newCells = cells.toMutableList()
        newCells[row * cols + col] = value
        return this.copy(cells = newCells)
    }

    fun copyWithUpdates(updates: Map<Int, Double>): TuningMap {
        val newCells = cells.toMutableList()
        updates.forEach { (index, value) ->
            if (index in newCells.indices) {
                newCells[index] = value
            }
        }
        return this.copy(cells = newCells)
    }
}

enum class MapType {
    FUEL, BASE_MAP, INJECTOR, IGNITION
}
