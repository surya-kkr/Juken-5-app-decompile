package com.kkr.juken.domain.model.protocol

sealed class EcuResponse {
    data class Success(val data: Any) : EcuResponse()
    data class Error(val reason: String) : EcuResponse()
    object Ack : EcuResponse()
}
