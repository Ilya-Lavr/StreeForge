package org.example.engine

import org.example.checker.check
import org.example.config.ConfigManager
import org.example.config.UserConfig
import org.example.generator.GeneratorManager
import org.example.generator.printTest
import org.example.solveManagers.CorrectManager
import org.example.solveManagers.IncorrectManager

object Engine {

    fun initialize() {
        println("Initialize started")
        println("Print path to correct solve: ")
        val correctPath = readlnOrNull()
        println("Print path to incorrect solve: ")
        val incorrectPath = readlnOrNull()
        println("Print path to generator: ")
        val generatorPath = readlnOrNull()
        ConfigManager.save(
            UserConfig(
                correctPath!!,
                incorrectPath!!,
                generatorPath!!,
            )
        )
    }

    fun run() {

        println("--- Stress test is started ---")

        val config = ConfigManager.load()
        CorrectManager.init(config.correctSolvePath)
        IncorrectManager.init(config.incorrectSolvePath)


        (1..500).forEach { _ ->
            GeneratorManager.generate(config.generatorPath)

            val correct = CorrectManager.run()
            if (correct.exitCode != 0) {
                println("Correct solve failed")
                printTest()
                return
            }

            val incorrect = IncorrectManager.run()
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