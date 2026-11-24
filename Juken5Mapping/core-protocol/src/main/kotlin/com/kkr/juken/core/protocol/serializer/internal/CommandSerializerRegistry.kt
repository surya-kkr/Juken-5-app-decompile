package com.kkr.juken.core.protocol.serializer.internal

import com.kkr.juken.core.protocol.model.EcuCommand
import kotlin.reflect.KClass

/**
 * Registry for individual command serializers.
 */
class CommandSerializerRegistry {
    private val serializers: Map<KClass<out EcuCommand>, IndividualSerializer<*>> = mapOf(
        EcuCommand.Handshake::class to HandshakeSerializer(),
        EcuCommand.FetchGlobalVariables::class to GlobalVarsSerializer(),
        EcuCommand.ReadMapBlock::class to MapReadSerializer(),
        EcuCommand.ReadHistory::class to HistoryReadSerializer(),
        EcuCommand.ReadPattern::class to PatternReadSerializer(),
        EcuCommand.Heartbeat::class to HeartbeatSerializer()
    )

    @Suppress("UNCHECKED_CAST")
    fun <T : EcuCommand> getSerializer(command: T): IndividualSerializer<T> {
        return (serializers[command::class] ?: UnsupportedCommandSerializer()) as IndividualSerializer<T>
    }
}
