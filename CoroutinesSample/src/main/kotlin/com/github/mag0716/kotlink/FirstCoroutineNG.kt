package com.github.mag0716.kotlink

import kotlinx.coroutines.delay
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    thread {
        // Compile Error
        // Suspend functions are only allowed to be called from a coroutine or another suspend function
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    Thread.sleep(2000L)
}