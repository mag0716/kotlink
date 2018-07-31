package com.github.mag0716.kotlink

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    launch {
        repeat(1000) { i ->
            println("I'm sleeping $i")
            delay(500L)
        }
    }
    delay(1300L)
}