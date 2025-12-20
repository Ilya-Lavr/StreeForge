package org.example.solveManagers

import org.example.generator.GeneratorManager
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class SolveManager(val path: String, val timeLimitMS : Long) {

    fun run() : Result {
        val file = File(path)

        val process = ProcessBuilder(path)
            .directory(file.parentFile)
            .start()

        GeneratorManager.writeTo(process)

        val isFinished = process.waitFor(timeLimitMS, TimeUnit.MILLISECONDS)
        if (!isFinished) {
            process.destroyForcibly()
            return Result(
                stdout = "",
                stderr = "The program exceeded the time limit of $timeLimitMS milliseconds",
                exitCode = -1000
            )
        }

        val stdout = process.inputStream
            .bufferedReader(StandardCharsets.UTF_8)
            .readText()
            .trim()

        val stderr = process.inputStream
            .bufferedReader(StandardCharsets.UTF_8)
            .readText()
            .trim()

        return Result(
            stdout = stdout,
            stderr = stderr,
            exitCode = process.exitValue()
        )
    }
}