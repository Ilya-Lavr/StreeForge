import org.example.checker.check
import org.example.config.ConfigManager
import org.example.config.UserConfig
import org.example.generator.GeneratorManager
import org.example.solveManagers.SolveManager
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StressForgeTest {

    fun StartFind(config: UserConfig) : String {
        val correctManager = SolveManager(config.correctSolvePath, config.timeLimit)
        val incorrectManager = SolveManager(config.incorrectSolvePath, config.timeLimit)

        (1..500).forEach { _ ->
            GeneratorManager.generate(config.generatorPath)

            val correct = correctManager.run()
            if (correct.exitCode != 0) {
                return "RE..."
            }

            val incorrect = incorrectManager.run()
            if (incorrect.stderr.contains("time limit")) {
                return "TL"
            }
            if (incorrect.exitCode != 0) {
                return "RE"
            }

            if (!check(correct.stdout, incorrect.stdout)) {
                return "WA"
            }
        }
        return "OK"
    }

//    @Test
//    @DisplayName("Программа корректно находит стресс-тест")
//    fun correctFindStressTest() {
//
//    }

    @Test
    @DisplayName("Программа корректно завершает TL решение(я)")
    fun timeLimitFind() {
        val configManager = ConfigManager(
            "C:\\Users\\mailt\\programming_projects\\kotlin_projects\\StreeForge\\config.json"
        )
        val result = StartFind(configManager.load())
        assertEquals(result, "TL")
    }
}