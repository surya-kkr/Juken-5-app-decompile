package com.kkr.juken.core.sync.internal

/**
 * Handles verification of ECU data after a write operation.
 */
class WriteVerifier {

    /**
     * Compares two blocks of data and returns true if they match.
     */
    fun verify(ecuData: List<Int>, localData: List<Int>): Boolean {
        if (ecuData.size != localData.size) return false
        return ecuData.indices.all { ecuData[it] == localData[it] }
    }
}
