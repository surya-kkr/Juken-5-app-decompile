package com.kkr.juken.domain.model.protocol

import com.kkr.juken.domain.model.tuning.MapType

sealed class EcuCommand {
    object FetchHandshake : EcuCommand()
    object FetchModel : EcuCommand()
    data class FetchMapSegment(val type: MapType, val coreId: Int, val index: Int) : EcuCommand()
    data class FetchHistory(val index: Int) : EcuCommand()
    data class WriteCell(val type: MapType, val index: Int, val value: Double) : EcuCommand()
    object Ping : EcuCommand()
}
