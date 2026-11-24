package com.kkr.juken.feature.reports.engine

import com.kkr.juken.core.protocol.model.EcuIdentity
import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.feature.reports.domain.export.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * High-level engine for managing report generation and state.
 */
class ReportEngine {
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val pdfPipeline = PdfAssemblyPipeline()
    private val csvExporter = CsvExporter()
    private val jsonExporter = JsonExporter()
    private val registry = ExportRegistry()

    fun generateFullReport(
        identity: EcuIdentity,
        recommendations: List<TuningRecommendation>
    ): ByteArray {
        _isGenerating.value = true
        try {
            return pdfPipeline.assemble(identity, recommendations)
        } finally {
            _isGenerating.value = false
        }
    }
}
