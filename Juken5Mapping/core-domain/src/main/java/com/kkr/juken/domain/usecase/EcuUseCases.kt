package com.kkr.juken.domain.usecase

import com.kkr.juken.domain.model.cache.CachePolicy
import com.kkr.juken.domain.model.tuning.MapType
import com.kkr.juken.domain.model.ui.ConnectionUiState
import kotlinx.coroutines.flow.StateFlow

/**
 * UseCase to establish a secure connection with the physical or simulated ECU.
 */
interface ConnectEcuUseCase {
    suspend operator fun invoke(address: String)
}

/**
 * UseCase to gracefully terminate the ECU session.
 */
interface DisconnectEcuUseCase {
    suspend operator fun invoke()
}

/**
 * UseCase to orchestrate the multi-stage synchronization of all map and identity data.
 */
interface SynchronizeEcuUseCase {
    suspend operator fun invoke(policy: CachePolicy = CachePolicy.USE_CACHE)
}

/**
 * UseCase to read a specific map from the hardware or local cache.
 */
interface ReadMapUseCase {
    suspend operator fun invoke(type: MapType, policy: CachePolicy = CachePolicy.USE_CACHE)
}

/**
 * UseCase to write calibration changes back to the ECU with bit-level verification.
 */
interface WriteMapUseCase {
    suspend operator fun invoke(type: MapType)
}
