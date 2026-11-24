package com.kkr.juken.core.protocol.parser

import com.kkr.juken.core.protocol.model.ProtocolError

/**
 * Sealed hierarchy representing the outcome of a parsing attempt.
 */
sealed interface ParserResult<out T> {
    /**
     * Parsing succeeded and produced a valid result (e.g. ProtocolNode or EcuResponse).
     */
    data class Success<T>(val value: T) : ParserResult<T>

    /**
     * Parsing failed due to protocol or structural errors.
     */
    data class Failure(val error: ProtocolError) : ParserResult<Nothing>
}
