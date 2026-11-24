package com.kkr.juken.core.sync.internal

import com.kkr.juken.domain.model.tuning.MapType

/**
 * Stores pre-write snapshots to support atomic rollbacks.
 */
class RollbackManager {
    private val snapshots = mutableMapOf<String, List<Int>>() // key: "type_index"

    fun capture(type: MapType, index: Int, data: List<Int>) {
        snapshots["${type}_$index"] = data
    }

    fun getSnapshot(type: MapType, index: Int): List<Int>? {
        return snapshots["${type}_$index"]
    }

    fun clear() {
        snapshots.clear()
    }
}
