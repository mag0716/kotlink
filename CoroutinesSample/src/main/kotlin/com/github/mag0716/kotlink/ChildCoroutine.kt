package com.github.mag0716.kotlink

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val request = launch {
        val job1 = launch {
            println("job1 : start")
            delay(1000L)
            println("job1 : end")
        }
        val job2 = launch(kotlin.coroutines.coroutineContext) {
            delay(100L)
            println("job2 : start")
            delay(1000L)
            println("job2 : end")
        }
        job1.join()
        job2.join()
    }
    delay(500L)
    // job1 は同じコンテキストなのでログは出力される
    // job2 は子供のコンテキストなのでキャンセルされログは出力されない
    request.cancel()
    delay(1000L)
    println("main : end")
}