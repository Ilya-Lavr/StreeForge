package org.example.engine

import org.example.checker.check
import org.example.config.ConfigManager
import org.example.config.UserConfig
import org.example.generator.GeneratorManager
import org.example.generator.printTest
import org.example.solveManagers.SolveManager

object Engine {

    fun initialize() {
        println("Initialize started")
        println("Print path to correct solve: ")
        val correctPath = readlnOrNull()
        println("Print path to incorrect solve: ")
        val incorrectPath = readlnOrNull()
        println("Print path to generator: ")
        val generatorPath = readlnOrNull()
        println("Print time limit in ms: ")
        val timeLimit = readlnOrNull()!!.toLong()
        println("Print memory limit in mb")
        val memoryLimit = readlnOrNull()!!.toInt()
        ConfigManager.save(
            UserConfig(
                correctPath!!,
                incorrectPath!!,
                generatorPath!!,
                timeLimit,
                memoryLimit
            )
        )
    }

    fun run() {

        println("--- Stress test is started ---")

        val config = ConfigManager.load()
        val correctManager = SolveManager(config.correctSolvePath, config.timeLimit)
        val incorrectManager = SolveManager(config.incorrectSolvePath, config.timeLimit)

        (1..500).forEach { _ ->
            GeneratorManager.generate(config.generatorPath)

            val correct = correctManager.run()
            if (correct.exitCode != 0) {
                println("Correct solve failed")
                printTest()
                return
            }

            val incorrect = incorrectManager.run()
            if (incorrect.exitCode != 0) {
                println("Runtime Error")
                printTest()
                return
            }

            if (!check(correct.stdout, incorrect.stdout)) {
                println("Wrong Answer")

                printTest()

                println("Correct output: ")
                println(correct.stdout)

                println("Incorrect output: ")
                println(incorrect.stdout)

                return
            }
        }

        println("The incorrect solution is correct")
    }
}