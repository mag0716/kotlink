package com.github.mag0716.kotlink

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            if(System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    // キャンセルしているが、launch 内でキャンセルされたかどうかチェックしていないので、5回出力される
    job.cancelAndJoin()
    println("main: Now I can quit.")
}