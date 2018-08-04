package com.github.mag0716.kotlink

import kotlinx.coroutines.CommonPool
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val request = launch(kotlin.coroutines.coroutineContext) {
        // デフォルトだと、ForkJoinPool.commonPool-worker-1 として実行され、キャンセルされない
        // 以下の実装だと、親のコンテキストを継承するのでキャンセルされる
        val job = launch(kotlin.coroutines.coroutineContext + CommonPool) {
            println("job : start : ${Thread.currentThread().name}")
            delay(1000L)
            println("job : end : ${Thread.currentThread().name}")
        }
        job.join()
    }
    delay(500L)
    request.cancel()
    delay(1000L)
    println("main : end")
}