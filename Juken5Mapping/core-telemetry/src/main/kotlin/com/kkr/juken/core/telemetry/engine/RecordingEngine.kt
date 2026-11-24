package com.kkr.juken.core.telemetry.engine

import com.kkr.juken.core.telemetry.engine.internal.BinaryFrameCodec
import com.kkr.juken.domain.model.telemetry.TelemetryFrame
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.DataOutputStream
import java.io.OutputStream

/**
 * Handles the recording of telemetry data to an [OutputStream].
 */
class RecordingEngine(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private var recordingJob: Job? = null

    /**
     * Starts recording frames from the source into the destination output.
     */
    fun start(source: FrameSource, destination: OutputStream) {
        if (_isRecording.value) return
        _isRecording.value = true

        val output = DataOutputStream(destination)
        
        recordingJob = scope.launch(dispatcher) {
            try {
                // Write Header
                writeHeader(output)
                
                source.frames.collect { frame ->
                    BinaryFrameCodec.writeFrame(frame, output)
                    output.flush()
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    // Handle error
                }
            } finally {
                withContext(NonCancellable) {
                    _isRecording.value = false
                    output.close()
                }
            }
        }
    }

    fun stop() {
        recordingJob?.cancel()
    }

    private fun writeHeader(output: DataOutputStream) {
        output.write("J5S".toByteArray(Charsets.US_ASCII)) // Magic
        output.writeInt(1) // Version
        output.writeLong(System.currentTimeMillis()) // Timestamp
    }
}
