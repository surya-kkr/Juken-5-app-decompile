package com.kkr.juken.domain.model

data class SelectionRange(
    val startRow: Int,
    val startCol: Int,
    val endRow: Int,
    val endCol: Int
) {
    val minRow get() = minOf(startRow, endRow)
    val maxRow get() = maxOf(startRow, endRow)
    val minCol get() = minOf(startCol, endCol)
    val maxCol get() = maxOf(startCol, endCol)

    fun contains(row: Int, col: Int): Boolean {
        return row in minRow..maxRow && col in minCol..maxCol
    }

    val indices: List<Int>
        get() {
            val list = mutableListOf<Int>()
            for (r in minRow..maxRow) {
                for (c in minCol..maxCol) {
                    list.add(r * 61 + c) // Assuming fixed column width of 61 for now
                }
            }
            return list
        }
}
