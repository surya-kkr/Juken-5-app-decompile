package com.kkr.juken.core.protocol.serializer

import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.serializer.internal.CommandSerializerRegistry
import com.kkr.juken.core.protocol.serializer.internal.PacketWriter

/**
 * Default implementation of [CommandSerializer] using a registry-based dispatch.
 */
class CommandSerializerImpl(
    private val registry: CommandSerializerRegistry = CommandSerializerRegistry(),
    private val writer: PacketWriter = PacketWriter()
) : CommandSerializer {

    override fun serialize(command: EcuCommand): String {
        val serializer = registry.getSerializer(command)
        return serializer.serialize(command, writer)
    }
}
