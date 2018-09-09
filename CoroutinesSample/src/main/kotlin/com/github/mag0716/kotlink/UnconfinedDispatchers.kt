package com.github.mag0716.kotlink

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    val jobs = arrayListOf<Job>()
    // 中断関数である delay 前は main スレッドで実行される
    jobs += launch(Unconfined) {
        println("Unconfined start : ${Thread.currentThread().name}")
        delay(500L)
        println("Unconfined end : ${Thread.currentThread().name}")
    }
    // どちらも main スレッドで実行される
    jobs += launch(kotlin.coroutines.coroutineContext) {
        println("coroutineContext start : ${Thread.currentThread().name}")
        delay(500L)
        println("coroutineContext end : ${Thread.currentThread().name}")
    }
    jobs.forEach { it.join() }
}