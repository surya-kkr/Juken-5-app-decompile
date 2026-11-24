package com.kkr.juken.mock.ecu.internal

/**
 * Independent codec for the ECU simulator to prevent implementation mirroring bugs.
 * Handles the raw conversion between domain objects and ASCII bytes for the simulation.
 */
internal class SimulatorCodec {

    fun decodeCommand(raw: String): SimulatorRequest {
        val fields = raw.trim().split(";")
        val header = fields.firstOrNull() ?: return SimulatorRequest.Unknown(raw)
        
        return when (header) {
            "1616" -> SimulatorRequest.Handshake
            "1606" -> SimulatorRequest.FetchGlobalVariables
            "1601" -> decodeReadMap(fields, "BASE")
            "1602" -> decodeReadMap(fields, "FUEL")
            "1603" -> decodeReadMap(fields, "INJECTOR")
            "1605" -> decodeReadMap(fields, "IGNITION")
            "1610" -> decodeReadHistory(fields)
            "1614" -> decodeReadPattern(fields)
            "1B00" -> SimulatorRequest.Heartbeat
            else -> SimulatorRequest.Unknown(raw)
        }
    }

    private fun decodeReadMap(fields: List<String>, type: String): SimulatorRequest {
        if (fields.size < 3) return SimulatorRequest.Unknown(fields.joinToString(";"))
        val core = fields[1].toIntOrNull() ?: 1
        val block = fields[2].toIntOrNull() ?: 0
        return SimulatorRequest.ReadMap(type, core, block)
    }

    private fun decodeReadHistory(fields: List<String>): SimulatorRequest {
        val block = fields.getOrNull(1)?.toIntOrNull() ?: 0
        return SimulatorRequest.ReadHistory(block)
    }

    private fun decodeReadPattern(fields: List<String>): SimulatorRequest {
        val core = fields.getOrNull(1)?.toIntOrNull() ?: 1
        val block = fields.getOrNull(2)?.toIntOrNull() ?: 0
        return SimulatorRequest.ReadPattern(core, block)
    }

    fun encodeResponse(header: String, vararg fields: Any): ByteArray {
        val payload = if (fields.isEmpty()) header else "$header;${fields.joinToString(";")}"
        return "$payload\r\n".toByteArray(Charsets.US_ASCII)
    }
}

internal sealed interface SimulatorRequest {
    object Handshake : SimulatorRequest
    object FetchGlobalVariables : SimulatorRequest
    data class ReadMap(val type: String, val core: Int, val block: Int) : SimulatorRequest
    data class ReadHistory(val block: Int) : SimulatorRequest
    data class ReadPattern(val core: Int, val blockIndex: Int) : SimulatorRequest
    object Heartbeat : SimulatorRequest
    data class Unknown(val raw: String) : SimulatorRequest
}
