package com.kkr.juken.core.sync.internal

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ActiveCellTrackerTest {

    private val tracker = ActiveCellTracker(rows = 21, cols = 61)

    @Test
    fun `findActiveIndex should return correct index for known breakpoints`() {
        // RPM 0, TPS 0 -> Index 0
        assertEquals(0, tracker.findActiveIndex(0, 0.0))
        
        // RPM 500, TPS 0 -> Index 61 (Row 1, Col 0)
        assertEquals(61, tracker.findActiveIndex(500, 0.0))
        
        // RPM 1000, TPS 1.66 -> Index 123 (Row 2, Col 1 approx)
        // 1000 is index 2 in 21 breakpoints
        // 1.66 is index 1 in 61 breakpoints
        // 2 * 61 + 1 = 123
        assertEquals(123, tracker.findActiveIndex(1000, 1.666))
    }

    @Test
    fun `findInterpolationIndices should return 4 surrounding cells`() {
        // RPM 750 (between 500 and 1000), TPS 1.0 (between 0 and 1.66)
        val indices = tracker.findInterpolationIndices(750, 1.0)
        
        // Should contain (Row 1, Col 0), (Row 1, Col 1), (Row 2, Col 0), (Row 2, Col 1)
        assertTrue(indices.contains(1 * 61 + 0)) // 61
        assertTrue(indices.contains(1 * 61 + 1)) // 62
        assertTrue(indices.contains(2 * 61 + 0)) // 122
        assertTrue(indices.contains(2 * 61 + 1)) // 123
        assertEquals(4, indices.size)
    }
}
