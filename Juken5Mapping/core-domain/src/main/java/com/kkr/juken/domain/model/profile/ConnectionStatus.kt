package com.kkr.juken.domain.model.profile

sealed class ConnectionStatus {
    object Idle : ConnectionStatus()
    object Searching : ConnectionStatus()
    object Connecting : ConnectionStatus()
    data class Synchronizing(val step: String, val progress: Float) : ConnectionStatus()
    object Connected : ConnectionStatus()
    data class Error(val message: String) : ConnectionStatus()
}
