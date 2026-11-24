package com.kkr.juken.core.data.repository

import com.kkr.juken.domain.model.tuning.*
import com.kkr.juken.domain.repository.CalibrationRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalibrationRepositoryImpl @Inject constructor() : CalibrationRepository {

    private val _activeCalibration = MutableStateFlow<Map<MapType, TuningMap>>(emptyMap())
    override val activeCalibration: StateFlow<Map<MapType, TuningMap>> = _activeCalibration.asStateFlow()

    override suspend fun updateCell(type: MapType, index: Int, value: Int) {
        _activeCalibration.update { current ->
            val map = current[type] ?: return@update current
            val newCells = map.cells.toMutableList()
            if (index in newCells.indices) {
                newCells[index] = newCells[index].copy(value = value.toDouble())
            }
            current + (type to map.copy(cells = newCells))
        }
    }

    override suspend fun applyDelta(type: MapType, index: Int, delta: Int) {
        _activeCalibration.update { current ->
            val map = current[type] ?: return@update current
            val newCells = map.cells.toMutableList()
            if (index in newCells.indices) {
                val newValue = newCells[index].value + delta
                newCells[index] = newCells[index].copy(value = newValue)
            }
            current + (type to map.copy(cells = newCells))
        }
    }
}
