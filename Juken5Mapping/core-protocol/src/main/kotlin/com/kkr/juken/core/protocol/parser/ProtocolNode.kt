package com.kkr.juken.core.protocol.parser

/**
 * Intermediate representation of parsed protocol fields before mapping to domain responses.
 * Each implementation represents a specific packet family verified by reverse engineering.
 */
sealed interface ProtocolNode {

    /**
     * Raw data for Global Variables (9606).
     */
    data class GlobalVariablesNode(val variables: Map<String, String>) : ProtocolNode

    /**
     * Raw data for ECU Identity (9616).
     */
    data class IdentityNode(
        val modelName: String,
        val hardwareVer: String,
        val firmwareVer: String,
        val serialNumber: String? = null
    ) : ProtocolNode

    /**
     * Raw data for Base Map blocks (9601).
     */
    data class BaseMapNode(val blockIndex: Int, val values: List<Int>) : ProtocolNode

    /**
     * Raw data for Fuel Map blocks (9602).
     */
    data class FuelMapNode(val blockIndex: Int, val values: List<Int>) : ProtocolNode

    /**
     * Raw data for Injector Map blocks (9603).
     */
    data class InjectorMapNode(val blockIndex: Int, val values: List<Int>) : ProtocolNode

    /**
     * Raw data for Ignition Map blocks (9605).
     */
    data class IgnitionMapNode(val blockIndex: Int, val values: List<Int>) : ProtocolNode

    /**
     * Raw data for History blocks (9610).
     */
    data class HistoryNode(val blockIndex: Int, val rawEntries: List<String>) : ProtocolNode

    /**
     * Raw data for Auto Mapping Pattern blocks (9614).
     */
    data class PatternNode(val blockIndex: Int, val values: List<Int>) : ProtocolNode

    /**
     * Raw data for Calibration (A601).
     */
    data class CalibrationNode(val rawValue: String) : ProtocolNode

    /**
     * Raw data for Diagnostics (A602).
     */
    data class DiagnosticNode(
        val statusFlags: String,
        val dtcCount: Int,
        val rawCodes: List<String>
    ) : ProtocolNode

    /**
     * Raw data for Maintenance (B800).
     */
    data class MaintenanceNode(val rawValue: String) : ProtocolNode

    /**
     * Positive acknowledgment (1A00).
     */
    object AckNode : ProtocolNode
}
