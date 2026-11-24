package com.kkr.juken.feature.reports.domain.export

import com.kkr.juken.domain.model.autotune.TuningRecommendation

/**
 * Exports recommendation data to JSON format.
 */
class JsonExporter {
    
    fun export(recommendations: List<TuningRecommendation>): String {
        // For production, we'd use a real JSON library like GSON or Kotlin Serialization.
        // For this implementation, we simulate the output.
        return "{ \"recommendations\": [] }"
    }
}
