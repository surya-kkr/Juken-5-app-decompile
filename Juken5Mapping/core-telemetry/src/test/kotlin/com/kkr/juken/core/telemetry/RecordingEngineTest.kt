package com.kkr.juken.core.telemetry

import com.kkr.juken.core.telemetry.engine.*
import com.kkr.juken.domain.model.telemetry.*
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalCoroutinesApi::class)
class RecordingEngineTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val engine = RecordingEngine(testScope, testDispatcher)

    @Test
    fun `engine should write frames to stream`() = testScope.runTest {
        val frame = TelemetryFrame(
            timestamp = 1000,
            engine = EngineState(2000, 15.0, 101.0),
            electrical = ElectricalState(13.8),
            fuel = FuelState(14.7, 1.0, 14.7, 5.0),
            ignition = IgnitionState(15.0),
            environment = EnvironmentState(90),
            connection = ConnectionStats(10),
            metadata = FrameMetadata(0)
        )
        
        val frames = MutableSharedFlow<TelemetryFrame>()
        val source = mockk<FrameSource>()
        every { source.frames } returns frames
        
        val output = ByteArrayOutputStream()
        engine.start(source, output)
        
        frames.emit(frame)
        engine.stop()
        
        val bytes = output.toByteArray()
        assertTrue("Output should not be empty, got ${bytes.size} bytes", bytes.size > 15)
        assertTrue("Check magic J5S", bytes.sliceArray(0..2).contentEquals("J5S".toByteArray(Charsets.US_ASCII)))
    }
}
