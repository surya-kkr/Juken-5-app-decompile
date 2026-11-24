package com.kkr.juken.core.protocol.serializer

import com.kkr.juken.core.protocol.model.CommandId
import com.kkr.juken.core.protocol.model.EcuCommand
import com.kkr.juken.core.protocol.model.MapType
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Official Golden Tests for the Juken Protocol.
 * These tests ensure that domain objects map EXACTLY to the ASCII wire protocol
 * as documented in the reverse engineering specification.
 */
class GoldenProtocolTest {

    private val serializer: CommandSerializer = CommandSerializerImpl()
    private val id = CommandId("fixed-id")

    @Test
    fun `Golden Handshake`() {
        val cmd = EcuCommand.Handshake(id)
        assertGolden("1616\r\n", cmd)
    }

    @Test
    fun `Golden Global Variables Fetch`() {
        val cmd = EcuCommand.FetchGlobalVariables(id)
        assertGolden("1606\r\n", cmd)
    }

    @Test
    fun `Golden Read Fuel Map Block 0`() {
        val cmd = EcuCommand.ReadMapBlock(id, type = MapType.FUEL, coreIndex = 1, blockIndex = 0)
        assertGolden("1602;1;0\r\n", cmd)
    }

    @Test
    fun `Golden Read Base Map Block 5`() {
        val cmd = EcuCommand.ReadMapBlock(id, type = MapType.BASE_MAP, coreIndex = 1, blockIndex = 5)
        assertGolden("1601;1;5\r\n", cmd)
    }

    @Test
    fun `Golden Read Injector Map Block 10`() {
        val cmd = EcuCommand.ReadMapBlock(id, type = MapType.INJECTOR, coreIndex = 1, blockIndex = 10)
        assertGolden("1603;1;10\r\n", cmd)
    }

    @Test
    fun `Golden Read Ignition Map Block 20`() {
        val cmd = EcuCommand.ReadMapBlock(id, type = MapType.IGNITION, coreIndex = 1, blockIndex = 20)
        assertGolden("1605;1;20\r\n", cmd)
    }

    @Test
    fun `Golden Read History Block 2`() {
        val cmd = EcuCommand.ReadHistory(id, blockIndex = 2)
        assertGolden("1610;2\r\n", cmd)
    }

    @Test
    fun `Golden Read Pattern Block 5`() {
        val cmd = EcuCommand.ReadPattern(id, coreIndex = 1, blockIndex = 5)
        assertGolden("1614;1;5\r\n", cmd)
    }

    private fun assertGolden(expected: String, command: EcuCommand) {
        val actual = serializer.serialize(command)
        assertEquals("Golden mismatch for ${command::class.simpleName}", expected, actual)
    }
}
