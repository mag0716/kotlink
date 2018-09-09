package com.github.mag0716.kotlink

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    val job = launch {
        // try - catch しなくても、強制終了することはない
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } catch(e : CancellationException) {
            println("I'm catch $e")
        } finally {
            println("I'm running finally")
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}