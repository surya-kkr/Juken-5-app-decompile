package com.kkr.juken.feature.validation

/**
 * Compares responses from different hardware sources.
 */
class HardwareComparison {

    data class Diff(
        val field: String,
        val expected: String,
        val actual: String,
        val source: String
    )

    /**
     * Diffs two raw packets.
     */
    fun compareRaw(expected: String, actual: String): List<Diff> {
        val diffs = mutableListOf<Diff>()
        if (expected != actual) {
            val eFields = expected.split(";")
            val aFields = actual.split(";")
            
            val max = maxOf(eFields.size, aFields.size)
            for (i in 0 until max) {
                val ev = eFields.getOrNull(i) ?: "[MISSING]"
                val av = aFields.getOrNull(i) ?: "[MISSING]"
                if (ev != av) {
                    diffs.add(Diff("Field[$i]", ev, av, "RawPacket"))
                }
            }
        }
        return diffs
    }
}
