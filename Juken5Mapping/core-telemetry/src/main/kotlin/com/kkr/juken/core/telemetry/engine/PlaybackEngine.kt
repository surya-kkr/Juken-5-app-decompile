package com.kkr.juken.core.telemetry.engine

import com.kkr.juken.domain.model.telemetry.TelemetryFrame
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Orchestrates globally synchronized playback of recorded sessions.
 */
class PlaybackEngine(
    private val scope: CoroutineScope
) {
    private val _currentFrame = MutableStateFlow<TelemetryFrame?>(null)
    val currentFrame: StateFlow<TelemetryFrame?> = _currentFrame.asStateFlow()

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private var playbackJob: Job? = null

    fun play(frames: List<TelemetryFrame>, speed: Float = 1.0f) {
        stop()
        _playbackState.value = PlaybackState.PLAYING
        
        playbackJob = scope.launch {
            frames.forEach { frame ->
                _currentFrame.value = frame
                val delayMs = (100L / speed).toLong() // Simplified timing
                delay(delayMs)
            }
            _playbackState.value = PlaybackState.COMPLETED
        }
    }

    fun stop() {
        playbackJob?.cancel()
        _playbackState.value = PlaybackState.IDLE
    }
}

enum class PlaybackState { IDLE, PLAYING, PAUSED, COMPLETED }
