package com.kkr.juken.core.protocol.serializer.internal

import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.model.MapType

/**
 * Base interface for mapping specific [EcuCommand] types to protocol fields.
 */
interface IndividualSerializer<in T : EcuCommand> {
    fun serialize(command: T, writer: PacketWriter): String
}

/**
 * Verification Status: VERIFIED
 * Maps Handshake domain intent to protocol code 1616.
 * Expected Response: 9616 (ECU Identity)
 */
class HandshakeSerializer : IndividualSerializer<EcuCommand.Handshake> {
    override fun serialize(command: EcuCommand.Handshake, writer: PacketWriter): String {
        return writer.write("1616")
    }
}

/**
 * Verification Status: VERIFIED
 * Maps Global Variables fetch to protocol code 1606.
 * Expected Response: 9606 (Global Variables)
 */
class GlobalVarsSerializer : IndividualSerializer<EcuCommand.FetchGlobalVariables> {
    override fun serialize(command: EcuCommand.FetchGlobalVariables, writer: PacketWriter): String {
        return writer.write("1606")
    }
}

/**
 * Verification Status: VERIFIED
 * Maps Map Read intents to codes 1601 (Base), 1602 (Fuel), 1603 (Injector), 1605 (Ignition).
 */
class MapReadSerializer : IndividualSerializer<EcuCommand.ReadMapBlock> {
    override fun serialize(command: EcuCommand.ReadMapBlock, writer: PacketWriter): String {
        val header = when (command.type) {
            MapType.BASE_MAP -> "1601"
            MapType.FUEL -> "1602"
            MapType.INJECTOR -> "1603"
            MapType.IGNITION -> "1605"
        }
        return writer.write(header, command.coreIndex, command.blockIndex)
    }
}

/**
 * Verification Status: VERIFIED
 * Maps History Read intent to protocol code 1610.
 * Expected Response: 9610 (History)
 */
class HistoryReadSerializer : IndividualSerializer<EcuCommand.ReadHistory> {
    override fun serialize(command: EcuCommand.ReadHistory, writer: PacketWriter): String {
        return writer.write("1610", command.blockIndex)
    }
}

/**
 * Verification Status: VERIFIED
 * Maps Pattern Read intent to protocol code 1614.
 * Expected Response: 9614 (Pattern)
 */
class PatternReadSerializer : IndividualSerializer<EcuCommand.ReadPattern> {
    override fun serialize(command: EcuCommand.ReadPattern, writer: PacketWriter): String {
        return writer.write("1614", command.coreIndex, command.blockIndex)
    }
}

/**
 * Verification Status: VERIFIED
 * Maps Heartbeat intent to protocol code 1B00.
 */
class HeartbeatSerializer : IndividualSerializer<EcuCommand.Heartbeat> {
    override fun serialize(command: EcuCommand.Heartbeat, writer: PacketWriter): String {
        return writer.write("1B00")
    }
}

/**
 * Verification Status: UNSUPPORTED / UNKNOWN
 * Prevents serialization of unverified commands.
 */
class UnsupportedCommandSerializer : IndividualSerializer<EcuCommand> {
    override fun serialize(command: EcuCommand, writer: PacketWriter): String {
        throw UnsupportedOperationException(
            "Command ${command::class.simpleName} is not yet verified or supported in the protocol specification."
        )
    }
}
