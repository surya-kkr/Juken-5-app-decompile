package com.kkr.juken.core.protocol.model

import java.util.UUID

/**
 * A unique identifier for an [EcuCommand] instance.
 * This ID is used for internal application tracking (queuing, response matching)
 * and is not part of the physical ECU protocol.
 */
data class CommandId(val value: String = UUID.randomUUID().toString())
