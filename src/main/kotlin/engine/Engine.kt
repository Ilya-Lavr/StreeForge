package org.example.engine

import org.example.checker.check
import org.example.config.ConfigManager
//import org.example.config.ConfigManager
import org.example.config.UserConfig
import org.example.generator.GeneratorManager
import org.example.generator.printTest
import org.example.solveManagers.SolveManager

object Engine {

    val configManager = ConfigManager(
        "C:\\Users\\mailt\\programming_projects\\kotlin_projects\\StreeForge\\config.json"
    )

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
        configManager.save(
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

        for (i in 0..900000) {
            print("\r${i / 9000}%")
        }
        print("\r")

        val config = configManager.load()
        val correctManager = SolveManager(config.correctSolvePath, config.timeLimit)
        val incorrectManager = SolveManager(config.incorrectSolvePath, config.timeLimit)

        repeat(500) { _ ->
            GeneratorManager.generate(config.generatorPath)

            val correct = correctManager.run()
            if (correct.exitCode != 0) {
                println("Correct solve failed")
                printTest()
                return
            }

            val incorrect = incorrectManager.run()
            if (incorrect.stderr.contains("time limit")) {
                println("Time Limit")
                printTest()
                return
            }
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

                println("\n\n\nIncorrect output: ")
                println(incorrect.stdout)

                return
            }
        }

        println("The incorrect solution is correct")
    }
}