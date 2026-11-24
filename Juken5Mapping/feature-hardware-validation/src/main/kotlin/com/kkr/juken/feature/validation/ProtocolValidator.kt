package com.kkr.juken.feature.validation

import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.model.EcuResponse
import com.kkr.juken.core.protocol.parser.ParserContext
import com.kkr.juken.core.protocol.parser.ParserResult
import com.kkr.juken.feature.validation.model.ValidationRule

/**
 * Validates protocol compliance of raw ECU traffic.
 */
class ProtocolValidator(
    private val codec: ProtocolCodec
) {

    /**
     * Checks if a request-response pair adheres to verified protocol rules.
     */
    fun validateExchange(request: EcuCommand, rawResponse: String): List<ValidationRule> {
        val rules = mutableListOf<ValidationRule>()
        
        // 1. Parser Compatibility
        val context = ParserContext(
            protocolVersion = com.kkr.juken.core.protocol.model.ProtocolVersion.JUKEN_5,
            transportType = "Validation",
            connectionId = "val",
            timestamp = System.currentTimeMillis()
        )
        val result = codec.decode(rawResponse, context)
        
        when (result) {
            is ParserResult.Failure -> {
                rules.add(ValidationRule("Parser", "Codec must parse response", ValidationRule.Status.FAILED, result.error.message))
                return rules
            }
            is ParserResult.Success -> {
                rules.add(ValidationRule("Parser", "Codec must parse response", ValidationRule.Status.PASSED))
                val response = result.value
                
                // 2. Header Consistency
                validateHeader(request, response, rules)
                
                // 3. Field Constraints
                validateFields(response, rules)
            }
        }
        
        return rules
    }

    private fun validateHeader(req: EcuCommand, resp: EcuResponse, rules: MutableList<ValidationRule>) {
        val expected = when (req) {
            is EcuCommand.Handshake -> resp is EcuResponse.EcuIdentityResponse
            is EcuCommand.FetchGlobalVariables -> resp is EcuResponse.GlobalVariablesResponse
            is EcuCommand.ReadMapBlock -> when (req.type) {
                com.kkr.juken.core.protocol.model.MapType.FUEL -> resp is EcuResponse.FuelMapResponse
                com.kkr.juken.core.protocol.model.MapType.BASE_MAP -> resp is EcuResponse.BaseMapResponse
                com.kkr.juken.core.protocol.model.MapType.INJECTOR -> resp is EcuResponse.InjectorMapResponse
                com.kkr.juken.core.protocol.model.MapType.IGNITION -> resp is EcuResponse.IgnitionMapResponse
            }
            is EcuCommand.ReadHistory -> resp is EcuResponse.HistoryResponse
            is EcuCommand.Heartbeat -> resp is EcuResponse.AckResponse
            else -> true // Other commands not verified for header yet
        }
        
        val status = if (expected) ValidationRule.Status.PASSED else ValidationRule.Status.FAILED
        rules.add(ValidationRule("HeaderMatch", "Response must match Request type", status))
    }

    private fun validateFields(resp: EcuResponse, rules: MutableList<ValidationRule>) {
        when (resp) {
            is EcuResponse.FuelMapResponse -> {
                val status = if (resp.values.size == 64) ValidationRule.Status.PASSED else ValidationRule.Status.FAILED
                rules.add(ValidationRule("BlockSize", "Fuel block must have 64 cells", status, "Got ${resp.values.size}"))
            }
            is EcuResponse.EcuIdentityResponse -> {
                val status = if (resp.identity.ecuName.isNotEmpty()) ValidationRule.Status.PASSED else ValidationRule.Status.WARNING
                rules.add(ValidationRule("Identity", "ECU Name should not be empty", status))
            }
            else -> {}
        }
    }
}
