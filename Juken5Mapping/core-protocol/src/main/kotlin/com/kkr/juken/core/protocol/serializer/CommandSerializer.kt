package com.kkr.juken.core.protocol.serializer

import com.kkr.juken.core.protocol.model.EcuCommand

/**
 * Interface for converting high-level [EcuCommand] objects into wire-ready ASCII strings.
 */
interface CommandSerializer {
    /**
     * Serializes the given command into a protocol-compliant string.
     */
    fun serialize(command: EcuCommand): String
}
