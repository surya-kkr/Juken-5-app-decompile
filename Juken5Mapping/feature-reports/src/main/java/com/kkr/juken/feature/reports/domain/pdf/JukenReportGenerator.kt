package com.kkr.juken.feature.reports.domain.pdf

import com.kkr.juken.core.protocol.model.EcuIdentity
import com.kkr.juken.domain.model.autotune.TuningRecommendation

/**
 * Orchestrates the generation of professional tuning reports.
 */
class JukenReportGenerator {

    data class ReportContext(
        val identity: EcuIdentity,
        val sessionDurationMs: Long,
        val recommendations: List<TuningRecommendation>,
        val acceptedChangesCount: Int,
        val overallConfidence: Float
    )

    fun generateSummary(context: ReportContext): String {
        return """
            JUKEN 5 TUNING REPORT
            ---------------------
            ECU: ${context.identity.ecuName}
            Firmware: ${context.identity.firmwareVersion.raw}
            
            STATISTICS
            ----------
            Total Recommendations: ${context.recommendations.size}
            Accepted Changes: ${context.acceptedChangesCount}
            Overall Confidence: ${(context.overallConfidence * 100).toInt()}%
        """.trimIndent()
    }
}
