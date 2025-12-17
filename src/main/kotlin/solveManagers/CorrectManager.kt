package org.example.solveManagers

import org.example.generator.GeneratorManager
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

object CorrectManager {
    private const val TIME_LIMIT_MS = 1000L
    private lateinit var path : String

    fun init(correctSolvePath : String) {
        path = correctSolvePath
    }

    fun run() : Result {
        val file = File(path)

        val process = ProcessBuilder(path)
            .directory(file.parentFile)
            .start()

        GeneratorManager.writeTo(process)

        val isFinished = process.waitFor(TIME_LIMIT_MS, TimeUnit.MILLISECONDS)
        if (!isFinished) {
            process.destroyForcibly()
            return Result(
                stdout = "",
                stderr = "The program exceeded the time limit of $TIME_LIMIT_MS milliseconds",
                exitCode = -1
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