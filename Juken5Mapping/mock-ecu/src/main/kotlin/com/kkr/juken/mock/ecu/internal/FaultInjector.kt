package com.kkr.juken.mock.ecu.internal

import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Strategy for injecting artificial failures into the simulated communication.
 */
internal class FaultInjector {
    
    var packetLossChance: Double = 0.0
    var corruptionChance: Double = 0.0
    var duplicateChance: Double = 0.0
    var minLatencyMs: Long = 0
    var maxLatencyMs: Long = 0

    /**
     * Intercepts a response and applies faults.
     * Returns null if the packet is dropped (loss).
     */
    suspend fun inject(data: ByteArray): List<ByteArray>? {
        // 1. Latency
        if (maxLatencyMs > 0) {
            delay(Random.nextLong(minLatencyMs, maxLatencyMs))
        }

        // 2. Packet Loss
        if (Random.nextDouble() < packetLossChance) return null

        // 3. Corruption
        val processedData = if (Random.nextDouble() < corruptionChance) {
            val corrupted = data.copyOf()
            if (corrupted.isNotEmpty()) {
                corrupted[Random.nextInt(corrupted.size)] = 0xFF.toByte()
            }
            corrupted
        } else {
            data
        }

        // 4. Duplication
        return if (Random.nextDouble() < duplicateChance) {
            listOf(processedData, processedData)
        } else {
            listOf(processedData)
        }
    }
}
