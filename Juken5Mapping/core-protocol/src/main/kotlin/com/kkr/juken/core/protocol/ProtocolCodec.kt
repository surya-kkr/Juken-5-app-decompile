package com.kkr.juken.core.protocol

import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.model.EcuResponse
import com.kkr.juken.core.protocol.parser.ParserContext
import com.kkr.juken.core.protocol.parser.ParserResult

/**
 * Unified interface for ECU communication encoding and decoding.
 */
interface ProtocolCodec {
    fun serialize(command: EcuCommand): String
    fun decode(raw: String, context: ParserContext): ParserResult<EcuResponse>

    companion object {
        fun createJuken5(): ProtocolCodec = Juken5Codec()
    }
}
