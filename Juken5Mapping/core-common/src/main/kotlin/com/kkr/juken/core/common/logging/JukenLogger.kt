package com.kkr.juken.core.common.logging

/**
 * Unified logging utility for the Juken application.
 */
object JukenLogger {
    
    enum class Level { DEBUG, INFO, WARN, ERROR }
    enum class Tag { PROTOCOL, REPOSITORY, TELEMETRY, UI, FLASHING, SYSTEM }

    private var logger: Logger? = null

    fun setLogger(l: Logger) {
        logger = l
    }

    fun d(tag: Tag, message: String) = log(Level.DEBUG, tag, message)
    fun i(tag: Tag, message: String) = log(Level.INFO, tag, message)
    fun w(tag: Tag, message: String) = log(Level.WARN, tag, message)
    fun e(tag: Tag, message: String, throwable: Throwable? = null) = log(Level.ERROR, tag, message, throwable)

    private fun log(level: Level, tag: Tag, message: String, throwable: Throwable? = null) {
        logger?.log(level, tag.name, message, throwable)
        // Fallback for simple JVM testing if no logger is set
        if (logger == null) {
            val ts = System.currentTimeMillis()
            println("[$ts] [${level.name}] [${tag.name}] $message")
            throwable?.printStackTrace()
        }
    }

    interface Logger {
        fun log(level: Level, tag: String, message: String, throwable: Throwable?)
    }
}
