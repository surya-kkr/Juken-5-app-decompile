package com.kkr.juken.core.autotune.engine

import com.kkr.juken.core.analysis.HeatmapEngine
import com.kkr.juken.core.telemetry.engine.FrameSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Orchestrates the intelligent Auto Tune pipeline.
 */
class AutoTuneEngine(
    private val scope: CoroutineScope
) {
    private val fuelCorrection = FuelCorrectionEngine()
    private val confidence = ConfidenceEngine()
    private val heatmap = HeatmapEngine<Double>()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    fun runAnalysis(source: FrameSource) {
        _isAnalyzing.value = true
        scope.launch {
            try {
                source.frames.collect { frame ->
                    frame.metadata.activeCellIndex?.let { index ->
                        heatmap.record(index, frame.fuel.afr)
                    }
                }
                // Once finished, calculate results...
            } finally {
                _isAnalyzing.value = false
            }
        }
    }
}
