package com.kkr.juken.feature.reports.domain.export

import com.kkr.juken.domain.model.autotune.TuningRecommendation

/**
 * Exports recommendation data to CSV format.
 */
class CsvExporter {
    
    fun export(recommendations: List<TuningRecommendation>): String {
        val header = "ID,Cell,Original,Recommended,Confidence,Reason\n"
        val body = recommendations.joinToString("\n") { 
            "${it.id},${it.cellIndex},${it.originalValue},${it.recommendedValue},${it.confidence},${it.reason}"
        }
        return header + body
    }
}
