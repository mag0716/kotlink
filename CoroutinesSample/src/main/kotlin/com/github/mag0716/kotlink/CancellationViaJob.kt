package com.github.mag0716.kotlink

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    val job = Job()
    val coroutines = List(10) { i ->
        println("create coroutine $i")
        launch(kotlin.coroutines.coroutineContext, parent = job) {
            println("coroutine $i start")
            delay((i + 1) * 200L)
            println("coroutine $i end")
        }
    }
    println("launched coroutines ${coroutines.size}")
    delay(500L)
    println("Cancel via job")
    // 2つの coroutine 以外はキャンセルされる
    //job.cancelAndJoin()
    job.cancelAndJoin()
}