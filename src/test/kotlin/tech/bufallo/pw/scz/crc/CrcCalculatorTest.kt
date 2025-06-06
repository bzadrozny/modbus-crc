package tech.bufallo.pw.scz.crc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CrcCalculatorTest {

    companion object {
        @JvmStatic
        fun crcTestCases() = listOf(
            // message, expected CRC
            byteArrayOf() to 0xFFFF,
            byteArrayOf(0x01) to 0x807E,
            byteArrayOf(0x01, 0xAB.toByte(), 0x1D, 0x01, 0xFF.toByte(), 0x1) to 0x8EF3,
            byteArrayOf(0x01, 0x10, 0x00, 0x11, 0x00, 0x03, 0x06, 0x1A, 0xC4.toByte(), 0xBA.toByte(), 0xD0.toByte()) to 0x677F,
        )
    }

    @ParameterizedTest
    @MethodSource("crcTestCases")
    fun `should calculate correct CRC for given input`(testCase: Pair<ByteArray, Int>) {
        val (message, expectedCrc) = testCase
        println("Testing message: ${message.joinToString(", ") { "0x%02X".format(it) }} with expected CRC: 0x%04X".format(expectedCrc))

        val actualCrc = CrcCalculator.calculate(message)
        assertEquals(
            expectedCrc,
            actualCrc,
            "CRC mismatch. Expected CRC: 0x%04X. Actual CRC: 0x%04X".format(expectedCrc, actualCrc)
        )
    }
}
