package com.kkr.juken.core.protocol

import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.model.EcuResponse
import com.kkr.juken.core.protocol.model.ProtocolError
import com.kkr.juken.core.protocol.parser.ParserContext
import com.kkr.juken.core.protocol.parser.ParserRegistry
import com.kkr.juken.core.protocol.parser.ParserResult
import com.kkr.juken.core.protocol.parser.internal.PacketReader
import com.kkr.juken.core.protocol.serializer.CommandSerializer
import com.kkr.juken.core.protocol.serializer.CommandSerializerImpl

/**
 * Concrete implementation of [ProtocolCodec] for the Juken 5 ECU.
 * Unifies serialization and parsing for the communication layer.
 */
class Juken5Codec(
    private val serializer: CommandSerializer = CommandSerializerImpl(),
    private val registry: ParserRegistry = ParserRegistry(),
    private val reader: PacketReader = PacketReader()
) : ProtocolCodec {

    override fun serialize(command: EcuCommand): String {
        return serializer.serialize(command)
    }

    override fun decode(raw: String, context: ParserContext): ParserResult<EcuResponse> {
        return try {
            val packet = reader.read(raw)
            registry.parse(packet, context)
        } catch (e: Exception) {
            val message = e.message ?: "Read failed"
            ParserResult.Failure(ProtocolError.MalformedResponse(raw, message))
        }
    }
}
