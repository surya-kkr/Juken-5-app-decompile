package com.kkr.juken.mock.ecu.model

/**
 * Metadata for stateful ECU maps.
 *
 * @property isDirty True if the map has been modified but not yet "synchronized" (for simulation logic).
 * @property lastModified Timestamp of the last cell update.
 * @property checksum Current CRC or checksum placeholder.
 */
data class MapMetadata(
    val isDirty: Boolean = false,
    val lastModified: Long = System.currentTimeMillis(),
    val checksum: Int = 0
)

/**
 * Base implementation for simulated ECU maps with metadata.
 */
abstract class StatefulMap(val totalCells: Int) {
    private val _cells = IntArray(totalCells) { 0 }
    val cells: IntArray get() = _cells.copyOf()
    
    var metadata = MapMetadata()
        private set

    fun updateCell(index: Int, value: Int) {
        require(index in 0 until totalCells) { "Index $index out of bounds for map size $totalCells" }
        _cells[index] = value
        metadata = metadata.copy(isDirty = true, lastModified = System.currentTimeMillis())
    }

    fun getBlock(blockIndex: Int, blockSize: Int = 64): List<Int> {
        val start = blockIndex * blockSize
        val end = minOf(start + blockSize, totalCells)
        if (start >= totalCells) return emptyList()
        return _cells.slice(start until end)
    }

    fun setBlock(blockIndex: Int, data: List<Int>, blockSize: Int = 64) {
        val start = blockIndex * blockSize
        data.forEachIndexed { i, value ->
            if (start + i < totalCells) {
                _cells[start + i] = value
            }
        }
        metadata = metadata.copy(isDirty = true, lastModified = System.currentTimeMillis())
    }
}

class FuelMap : StatefulMap(1281)
class BaseMap : StatefulMap(1281)
class InjectorMap : StatefulMap(1281)
class IgnitionMap : StatefulMap(651)
