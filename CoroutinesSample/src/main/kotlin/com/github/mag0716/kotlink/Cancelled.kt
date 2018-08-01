package com.github.mag0716.kotlink

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch {
        var nextPrintTime = startTime
        var i = 0
        // キャンセルされているかどうかをチェックする
        // キャンセルされていたら以降の処理は実行されない
        while (isActive) {
            if(System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}