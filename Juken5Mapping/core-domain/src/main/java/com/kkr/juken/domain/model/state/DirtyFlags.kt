package com.kkr.juken.domain.model.state

/**
 * High-performance bitmask-based dirty tracking.
 * Allows independent tracking of multiple application regions.
 */
@JvmInline
value class DirtyFlags(val value: Long = 0L) {
    
    companion object {
        const val NONE = 0L
        const val MAP = 1L shl 0
        const val PROJECT = 1L shl 1
        const val WORKSPACE = 1L shl 2
        const val SETTINGS = 1L shl 3
        const val AUTOTUNE = 1L shl 4
        const val REPORT = 1L shl 5
        const val FLASH = 1L shl 6
        const val DIAGNOSTICS = 1L shl 7
        const val SERVICE = 1L shl 8
        
        const val ALL = -1L
    }

    fun isDirty(flag: Long): Boolean = (value and flag) != 0L
    
    fun setDirty(flag: Long): DirtyFlags = DirtyFlags(value or flag)
    
    fun clearDirty(flag: Long): DirtyFlags = DirtyFlags(value and flag.inv())
    
    fun hasAny(): Boolean = value != 0L
}
