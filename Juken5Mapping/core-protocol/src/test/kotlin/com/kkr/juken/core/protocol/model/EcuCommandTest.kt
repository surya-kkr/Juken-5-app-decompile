package com.kkr.juken.core.protocol.model

import org.junit.Assert.assertEquals
import org.junit.Test

class EcuCommandTest {

    private val testId = CommandId("test")

    @Test
    fun `ReadMapBlock should hold correct parameters`() {
        val command = EcuCommand.ReadMapBlock(id = testId, type = MapType.FUEL, blockIndex = 5)
        
        assertEquals(MapType.FUEL, command.type)
        assertEquals(5, command.blockIndex)
        assertEquals(testId, command.id)
    }

    @Test
    fun `WriteMapBlock should hold correct parameters`() {
        val data = listOf(10, 20, 30)
        val command = EcuCommand.WriteMapBlock(id = testId, type = MapType.IGNITION, blockIndex = 10, data = data)
        
        assertEquals(MapType.IGNITION, command.type)
        assertEquals(10, command.blockIndex)
        assertEquals(data, command.data)
    }

    @Test
    fun `SetLiveMode should hold correct state`() {
        val start = EcuCommand.SetLiveMode(id = testId, enabled = true)
        val stop = EcuCommand.SetLiveMode(id = testId, enabled = false)
        
        assertEquals(true, start.enabled)
        assertEquals(false, stop.enabled)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `ReadMapBlock should reject negative index`() {
        EcuCommand.ReadMapBlock(type = MapType.FUEL, blockIndex = -1)
    }
}
