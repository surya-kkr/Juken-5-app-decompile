package com.kkr.juken.feature.mapping.domain.engine.internal

/**
 * Tracks modified cell indices for visual highlighting and synchronization logic.
 */
internal class DirtyTracker {
    private val dirtyIndices = mutableSetOf<Int>()

    fun markDirty(index: Int) {
        dirtyIndices.add(index)
    }

    fun markDirty(indices: Collection<Int>) {
        dirtyIndices.addAll(indices)
    }

    fun isDirty(index: Int): Boolean = dirtyIndices.contains(index)

    fun clear() {
        dirtyIndices.clear()
    }

    fun getDirtyCount(): Int = dirtyIndices.size
    
    fun snapshot(): Set<Int> = dirtyIndices.toSet()
}
