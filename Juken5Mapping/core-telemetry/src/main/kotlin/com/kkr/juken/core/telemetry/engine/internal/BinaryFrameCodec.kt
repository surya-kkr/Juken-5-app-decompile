package com.kkr.juken.core.telemetry.engine.internal

import com.kkr.juken.domain.model.telemetry.*
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Handles binary serialization of TelemetryFrame for the .j5s format.
 */
internal object BinaryFrameCodec {
    private const val FRAME_VERSION = 1

    fun writeFrame(frame: TelemetryFrame, output: DataOutputStream) {
        output.writeLong(frame.timestamp)
        
        // Engine
        output.writeInt(frame.engine.rpm)
        output.writeDouble(frame.engine.tps)
        output.writeDouble(frame.engine.manifoldPressure)
        
        // Fuel
        output.writeDouble(frame.fuel.afr)
        output.writeDouble(frame.fuel.injectorDuty)
        
        // Electrical
        output.writeDouble(frame.electrical.batteryVoltage)
        
        // Env
        output.writeInt(frame.environment.coolantTemp)
        
        // Metadata
        output.writeInt(frame.metadata.activeCellIndex ?: -1)
    }

    fun readFrame(input: DataInputStream): TelemetryFrame {
        val ts = input.readLong()
        
        val engine = EngineState(input.readInt(), input.readDouble(), input.readDouble())
        val fuel = FuelState(input.readDouble(), 0.0, 0.0, input.readDouble())
        val electrical = ElectricalState(input.readDouble())
        val env = EnvironmentState(input.readInt())
        
        val activeCell = input.readInt().let { if (it == -1) null else it }
        
        return TelemetryFrame(
            timestamp = ts,
            engine = engine,
            fuel = fuel,
            electrical = electrical,
            ignition = IgnitionState(0.0),
            environment = env,
            connection = ConnectionStats(0),
            metadata = FrameMetadata(activeCellIndex = activeCell)
        )
    }
}
