package com.kkr.juken.mock.ecu.model

/**
 * Central state of the ECU simulator.
 */
class EngineState {
    val fuelMap = FuelMap()
    val baseMap = BaseMap()
    val injectorMap = InjectorMap()
    val ignitionMap = IgnitionMap()

    var rpm: Int = 0
    var tps: Double = 0.0
    var manifoldPressure: Double = 0.0
    var batteryVoltage: Double = 12.6
    var temperature: Int = 30

    val historyBuffer = mutableListOf<String>()
    val patternBuffer = mutableListOf<Int>()

    // Global variables (9606)
    val variables = mutableMapOf(
        "rev_limit" to "16000",
        "warming_up" to "1000",
        "idle_rpm" to "1200",
        "core" to "1"
    )

    // Hardware Identity (9616)
    var modelName: String = "Juken 5 Dualband"
    var hardwareVer: String = "1.0.0"
    var firmwareVer: String = "2.3.4"
    var serialNumber: String = "J5-TEST-0001"
}
