package com.kkr.juken.domain.model.profile

import java.util.UUID

/**
 * User-defined profile for a specific bike/ECU combination.
 */
data class EcuProfile(
    val id: UUID = UUID.randomUUID(),
    val nickname: String,
    val hardwareModel: EcuModel?,
    val macAddress: String,
    val lastSyncTimestamp: Long = 0L
)
