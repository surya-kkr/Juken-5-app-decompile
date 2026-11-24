package com.kkr.juken.core.protocol.model

/**
 * Base sealed interface representing any request sent to the ECU.
 * Commands represent the "intent" of the protocol and are independent of
 * transport or execution policies.
 */
sealed interface EcuCommand {
    /**
     * Unique identifier for this specific command instance.
     */
    val id: CommandId

    /**
     * Initial handshake command to establish communication and retrieve ECU identity.
     */
    data class Handshake(override val id: CommandId = CommandId()) : EcuCommand

    /**
     * Retrieves global engine variables and current state.
     */
    data class FetchGlobalVariables(override val id: CommandId = CommandId()) : EcuCommand

    /**
     * Reads a specific block of map data from the ECU.
     *
     * @property type The type of map to read (Fuel, Base Map, etc.).
     * @property coreIndex The cylinder or core index (1-based).
     * @property blockIndex The index of the memory block (typically 0-20).
     */
    data class ReadMapBlock(
        override val id: CommandId = CommandId(),
        val type: MapType,
        val coreIndex: Int = 1,
        val blockIndex: Int
    ) : EcuCommand {
        init {
            require(coreIndex >= 1) { "Core index must be at least 1" }
            require(blockIndex >= 0) { "Block index must be non-negative: $blockIndex" }
        }
    }

    /**
     * Writes a block of map data to the ECU.
     *
     * @property type The type of map to write.
     * @property coreIndex The cylinder or core index (1-based).
     * @property blockIndex The index of the memory block.
     * @property data The raw values to be written.
     */
    data class WriteMapBlock(
        override val id: CommandId = CommandId(),
        val type: MapType,
        val coreIndex: Int = 1,
        val blockIndex: Int,
        val data: List<Int>
    ) : EcuCommand {
        init {
            require(coreIndex >= 1) { "Core index must be at least 1" }
            require(blockIndex >= 0) { "Block index must be non-negative: $blockIndex" }
            require(data.isNotEmpty()) { "Data list cannot be empty" }
        }
    }

    /**
     * Command to enter or exit real-time data streaming mode.
     *
     * @property enabled True to start live data, false to stop.
     */
    data class SetLiveMode(
        override val id: CommandId = CommandId(),
        val enabled: Boolean
    ) : EcuCommand

    /**
     * Reads historical data blocks from the ECU (1610).
     *
     * @property blockIndex The index of the history block to read.
     */
    data class ReadHistory(
        override val id: CommandId = CommandId(),
        val blockIndex: Int
    ) : EcuCommand {
        init {
            require(blockIndex >= 0) { "History block index must be non-negative: $blockIndex" }
        }
    }

    /**
     * Reads auto mapping pattern blocks from the ECU (1614).
     *
     * @property blockIndex The index of the pattern block to read.
     */
    data class ReadPattern(
        override val id: CommandId = CommandId(),
        val coreIndex: Int = 1,
        val blockIndex: Int
    ) : EcuCommand {
        init {
            require(coreIndex >= 1)
            require(blockIndex >= 0)
        }
    }

    /**
     * Periodic heartbeat to maintain connection liveness.
     */
    data class Heartbeat(override val id: CommandId = CommandId()) : EcuCommand

    /**
     * Clears historical data (AFR History, etc.) from the ECU memory.
     */
    data class ClearHistory(override val id: CommandId = CommandId()) : EcuCommand

    /**
     * Performs a calibration action (e.g., reset TPS).
     */
    data class ResetTpsCalibration(override val id: CommandId = CommandId()) : EcuCommand
}

/**
 * Supported map types for read/write operations.
 */
enum class MapType {
    FUEL,
    BASE_MAP,
    INJECTOR,
    IGNITION
}
