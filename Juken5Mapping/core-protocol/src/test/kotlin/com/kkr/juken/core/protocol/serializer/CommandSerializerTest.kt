package com.kkr.juken.core.protocol.serializer

import com.kkr.juken.core.protocol.model.CommandId
import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.model.MapType
import org.junit.Assert.assertEquals
import org.junit.Test

class CommandSerializerTest {

    private val serializer: CommandSerializer = CommandSerializerImpl()
    private val testId = CommandId("test-id")

    @Test
    fun `Handshake should serialize to 1616`() {
        val result = serializer.serialize(EcuCommand.Handshake(testId))
        assertEquals("1616\r\n", result)
    }

    @Test
    fun `ReadMapBlock Fuel core 1 block 5 should serialize correctly`() {
        val cmd = EcuCommand.ReadMapBlock(id = testId, type = MapType.FUEL, coreIndex = 1, blockIndex = 5)
        val result = serializer.serialize(cmd)
        assertEquals("1602;1;5\r\n", result)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `WriteMapBlock should throw as it is unverified`() {
        val data = listOf(1, 2, 3)
        val cmd = EcuCommand.WriteMapBlock(id = testId, type = MapType.IGNITION, coreIndex = 2, blockIndex = 10, data = data)
        serializer.serialize(cmd)
    }

    @Test
    fun `ReadHistory should serialize to 1610 with index`() {
        val cmd = EcuCommand.ReadHistory(id = testId, blockIndex = 2)
        val result = serializer.serialize(cmd)
        assertEquals("1610;2\r\n", result)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `SetLiveMode should throw as it is unverified`() {
        val cmd = EcuCommand.SetLiveMode(id = testId, enabled = true)
        serializer.serialize(cmd)
    }
}
