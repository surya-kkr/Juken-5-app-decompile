package com.kkr.juken.core.autotune.engine

import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.model.tuning.MapType
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.system.measureTimeMillis

class FilterBenchmarkTest {

    @Test
    fun `benchmark filtering 100k items`() {
        val engine = SmartFilterEngine()
        val items = List(100_000) { i ->
            TuningRecommendation(
                id = i.toString(),
                mapType = MapType.FUEL,
                cellIndex = i % 1281,
                originalValue = 100,
                recommendedValue = 110,
                confidence = 0.95f,
                hitCount = 10,
                averageAfr = 15.0,
                targetAfr = 14.7,
                drivingMode = if (i % 2 == 0) "WOT" else "CRUISE",
                reason = "Reason $i",
                rpmRange = 0..1000,
                tpsRange = 0.0..10.0,
                temperatureAverage = 90.0,
                batteryAverage = 14.0,
                lambdaAverage = 1.0,
                interpolationWeight = 1.0
            )
        }
        
        val criteria = FilterCriteria(minConfidence = 0.9f, drivingModes = setOf("WOT"))
        
        val time = measureTimeMillis {
            engine.filter(items, criteria)
        }
        
        println("Filtering 100k items took ${time}ms")
        // Increased threshold for CI environment stability
        assertTrue("Filtering should be under 200ms (got ${time}ms)", time < 200)
    }
}
