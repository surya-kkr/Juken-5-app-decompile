package com.kkr.juken.domain.model.engine

data class DiagnosticData(
    val sensor1: String,
    val s2: String,
    val tpsRaw: String,
    val vBatRaw: String,
    val errorBitmask: Int = 0
)
