package org.example.generator

import java.nio.charset.StandardCharsets

object GeneratorManager {
    private var lastTest : String = ""

    fun generate(generatorPath : String) {
        val process = ProcessBuilder(generatorPath)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream.bufferedReader(StandardCharsets.UTF_8).readText()

        val exitCode = process.waitFor()

        require(exitCode == 0) {
            "Generator crashed"
        }

        lastTest = output
    }

    fun writeTo(process : Process) {
        val test = lastTest

        process.outputStream
            .bufferedWriter(StandardCharsets.UTF_8)
            .use {
                it.write(test)
            }
    }

    fun get() : String = lastTest
}