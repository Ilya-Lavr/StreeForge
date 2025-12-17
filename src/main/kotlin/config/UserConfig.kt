package org.example.config

import kotlinx.serialization.Serializable

@Serializable
data class UserConfig(
    val correctSolvePath : String,
    val incorrectSolvePath : String,
    val generatorPath : String,
)