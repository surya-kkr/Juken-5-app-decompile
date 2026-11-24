package com.kkr.juken.domain.model.tuning

data class TuningCell(
    val coordinate: CellCoordinate,
    val value: Double
) {
    // Basic validation based on general engine safety
    init {
        // Validation logic can be specific to MapType if needed via factory methods
    }
}
