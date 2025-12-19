package org.example

import org.example.engine.Engine

fun main() {
    println("=== Stress Forge started ===")
    while (true) {
        val command = readlnOrNull()
        when (command) {
            "--init" -> Engine.initialize()
            "--start" -> Engine.run()
            "--run" -> Engine.run()
//            "--setTL" ->
//            "--setML" ->
//            "--compilation" ->
            "--exit" -> return
            else -> println("Unknow command")
        }
    }
}