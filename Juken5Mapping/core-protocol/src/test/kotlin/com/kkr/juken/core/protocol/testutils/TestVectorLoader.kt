package com.kkr.juken.core.protocol.testutils

import java.io.File

/**
 * Loads protocol test vectors from the resources directory.
 */
object TestVectorLoader {

    fun loadResponse(fileName: String): List<String> {
        val resource = javaClass.classLoader.getResource("vectors/response/$fileName")
            ?: throw IllegalArgumentException("Test vector not found: $fileName")
        return File(resource.toURI()).readLines().filter { it.isNotBlank() }
    }
}
