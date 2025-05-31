package tech.bufallo.pw.scz.cmd

import com.github.ajalt.clikt.core.CliktCommand
import tech.bufallo.pw.scz.crc.CrcCalculator

class WarmUpCmd: CliktCommand(name = "warmup") {

    override fun run() {
        repeat(1000) {
            //generate a random byte array
            val bytes = ByteArray(256) { (0..255).random().toByte() }
            //calculate CRC
            CrcCalculator.calculate(bytes)
        }
    }
}