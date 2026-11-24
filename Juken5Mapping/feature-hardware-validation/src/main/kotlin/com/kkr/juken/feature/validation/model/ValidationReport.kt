package com.kkr.juken.feature.validation.model

/**
 * Results of a protocol or hardware validation session.
 */
data class ValidationReport(
    val fingerprint: String,
    val passedRules: List<ValidationRule>,
    val failedRules: List<ValidationRule>,
    val warnings: List<String>,
    val unknownPackets: Int = 0,
    val averageTurnaroundMs: Long = 0
)

data class ValidationRule(
    val name: String,
    val description: String,
    val status: Status,
    val details: String? = null
) {
    enum class Status { PASSED, FAILED, WARNING }
}
