package com.kkr.juken.core.protocol.model

/**
 * Sealed hierarchy representing errors that occur at the protocol level.
 * These are distinct from transport errors (e.g., Bluetooth disconnected).
 */
sealed class ProtocolError : Throwable() {

    /**
     * The response received from the ECU does not match the expected format.
     */
    data class MalformedResponse(val rawData: String, override val message: String) : ProtocolError()

    /**
     * The response checksum or integrity check failed.
     */
    object InvalidChecksum : ProtocolError() {
        override val message: String = "Response checksum mismatch"
    }

    /**
     * The ECU explicitly rejected the command.
     */
    data class CommandRejected(val reason: String?) : ProtocolError() {
        override val message: String = "ECU rejected command: ${reason ?: "Unknown reason"}"
    }

    /**
     * The command sent is not supported by the current ECU protocol version or capabilities.
     */
    data class UnsupportedCommand(val command: EcuCommand) : ProtocolError() {
        override val message: String = "Command not supported: ${command::class.simpleName}"
    }

    /**
     * An unexpected response was received for the given command.
     */
    data class UnexpectedResponse(val expected: String, val actual: String) : ProtocolError() {
        override val message: String = "Expected $expected but received $actual"
    }
}
