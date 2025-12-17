package org.example.config

import kotlinx.serialization.json.Json
import java.io.File

object ConfigManager {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val configFile = File("config.json")

    fun save(config : UserConfig) {
        val text = json.encodeToString(
            UserConfig.serializer(),
            config
        )
        configFile.writeText(text)
    }

    fun load() : UserConfig {
        val text = configFile.readText()
        return json.decodeFromString(
            UserConfig.serializer(),
            text
        )
    }
}