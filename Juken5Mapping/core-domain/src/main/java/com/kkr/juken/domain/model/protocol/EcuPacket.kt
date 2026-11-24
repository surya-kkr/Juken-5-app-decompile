package com.kkr.juken.domain.model.protocol

data class EcuPacket(
    val header: String,
    val payload: List<String>,
    val raw: String
)
