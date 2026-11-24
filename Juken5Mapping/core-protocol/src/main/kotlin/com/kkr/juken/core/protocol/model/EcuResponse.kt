package com.kkr.juken.core.protocol.model

/**
 * Base sealed interface representing any response received from the ECU.
 * Responses provide structured domain data and are parser-independent.
 */
sealed interface EcuResponse {

    /**
     * Response containing ECU identity information (9616).
     */
    data class EcuIdentityResponse(val identity: EcuIdentity) : EcuResponse

    /**
     * Generic acknowledgment that a command was received and processed (1A00).
     */
    object AckResponse : EcuResponse

    /**
     * Negative acknowledgment indicating a command failure or invalid parameters.
     */
    data class NackResponse(val reason: String? = null) : EcuResponse

    /**
     * Response containing global variables and engine status (9606).
     */
    data class GlobalVariablesResponse(val variables: Map<String, String>) : EcuResponse

    /**
     * Response containing a block of Fuel Map data (9602).
     */
    data class FuelMapResponse(val blockIndex: Int, val values: List<Int>) : EcuResponse {
        init { require(blockIndex >= 0) }
    }

    /**
     * Response containing a block of Base Map data (9601).
     */
    data class BaseMapResponse(val blockIndex: Int, val values: List<Int>) : EcuResponse {
        init { require(blockIndex >= 0) }
    }

    /**
     * Response containing a block of Injector Map data (9603).
     */
    data class InjectorMapResponse(val blockIndex: Int, val values: List<Int>) : EcuResponse {
        init { require(blockIndex >= 0) }
    }

    /**
     * Response containing a block of Ignition Map data (9605).
     */
    data class IgnitionMapResponse(val blockIndex: Int, val values: List<Int>) : EcuResponse {
        init { require(blockIndex >= 0) }
    }

    /**
     * Real-time engine telemetry data.
     */
    data class LiveDataResponse(
        val rpm: Int,
        val tps: Double,
        val temperature: Int,
        val batteryVoltage: Double,
        val manifoldPressure: Double? = null
    ) : EcuResponse {
        init {
            require(rpm >= 0)
            require(tps in 0.0..100.0)
        }
    }

    /**
     * Diagnostic data containing trouble codes or sensor health (A602).
     */
    data class DiagnosticResponse(
        val statusFlags: Int,
        val activeDtcCount: Int,
        val errorCodes: List<String>
    ) : EcuResponse

    /**
     * Response containing historical data entries (9610).
     */
    data class HistoryResponse(
        val blockIndex: Int,
        val entries: List<HistoryEntry>
    ) : EcuResponse {
        init { require(blockIndex >= 0) }
    }

    /**
     * Response containing auto mapping pattern data (9614).
     */
    data class PatternResponse(
        val blockIndex: Int,
        val values: List<Int>
    ) : EcuResponse {
        init { require(blockIndex >= 0) }
    }

    /**
     * Calibration data response (A601).
     */
    data class CalibrationResponse(
        val value: Int // Specific fields to be refined based on A601 layout
    ) : EcuResponse

    /**
     * Maintenance data response (B800).
     */
    data class MaintenanceResponse(
        val value: Int // Specific fields to be refined based on B800 layout
    ) : EcuResponse
}
