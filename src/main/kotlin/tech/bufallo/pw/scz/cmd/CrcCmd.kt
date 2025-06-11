package tech.bufallo.pw.scz.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.validate
import tech.bufallo.pw.scz.crc.CrcCalculator

class CrcCmd : CliktCommand(name = "crc") {

    private val input: String by argument("message", help = "Hex byte sequence (max 256 bytes, e.g. '01 10 00 11 BC ...')")
        .convert { it.replace(" ", "") }
        .validate { if (!validateHexString(it)) fail("Input sequence must be max 256 bytes and must be in hexadecimal notation, e.g. 01AF 2A D") }

    private val iterations: Long by option("--iterations", "-i", help = "Number of iterations to run (default: 10000)")
        .convert { it.toLong() }
        .default(1)
        .validate { if (it <= 0 || it > 1_000_000_000) fail("Iterations must be between 1 and 10^9") }

    override fun run() {
        val message = convertHexStringIntoByteArray(input)
        println("Message = %s".format(message.joinToString(" ") { "0x%02X".format(it) }))

        val processingIterations = iterations.toInt()
        println("Iterations = $iterations")

        var crc = 0
        val startTime = System.nanoTime()
        repeat(processingIterations) {
            crc = CrcCalculator.calculate(message)
        }
        val endTime = System.nanoTime()

        println("CRC = 0x%04X".format(crc))
        println("Time taken: %.6f seconds".format((endTime - startTime) / 1e9))
    }

    private fun validateHexString(input: String): Boolean {
        return input.all { it.isDigit() || (it in 'A'..'F') || (it in 'a'..'f') }
            && input.length <= 512 // 256 bytes * 2 hex digits per byte
    }

    private fun convertHexStringIntoByteArray(input: String): ByteArray = input
        .reversed()
        .chunked(2)
        .map { it.reversed().toUInt(16).toByte() }
        .reversed()
        .toByteArray()
}
