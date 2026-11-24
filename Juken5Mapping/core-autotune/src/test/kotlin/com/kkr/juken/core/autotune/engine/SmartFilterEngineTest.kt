package com.kkr.juken.core.autotune.engine

import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.model.tuning.MapType
import org.junit.Assert.assertEquals
import org.junit.Test

class SmartFilterEngineTest {

    private val engine = SmartFilterEngine()

    @Test
    fun `filter should exclude items below min confidence`() {
        val items = listOf(
            createRecommendation("1", 0.95f),
            createRecommendation("2", 0.50f)
        )
        val criteria = FilterCriteria(minConfidence = 0.90f)
        
        val result = engine.filter(items, criteria)
        
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
    }

    @Test
    fun `filter should match regex in reason`() {
        val items = listOf(
            createRecommendation("1", 1f, reason = "High Load Lean"),
            createRecommendation("2", 1f, reason = "Idle Rich")
        )
        val criteria = FilterCriteria(searchRegex = "Lean")
        
        val result = engine.filter(items, criteria)
        
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
    }

    private fun createRecommendation(id: String, confidence: Float, reason: String? = null) = TuningRecommendation(
        id = id,
        mapType = MapType.FUEL,
        cellIndex = 0,
        originalValue = 100,
        recommendedValue = 110,
        confidence = confidence,
        hitCount = 10,
        averageAfr = 15.0,
        targetAfr = 14.7,
        drivingMode = "CRUISE",
        rpmRange = 0..1000,
        tpsRange = 0.0..10.0,
        temperatureAverage = 90.0,
        batteryAverage = 14.0,
        lambdaAverage = 1.0,
        interpolationWeight = 1.0,
        reason = reason
    )
}
