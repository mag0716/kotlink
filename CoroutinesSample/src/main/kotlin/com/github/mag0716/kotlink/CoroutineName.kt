package com.github.mag0716.kotlink

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// -Dkotlinx.coroutines.debug 付きで実行すると、CoroutineName で指定した文字列が出力される
private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main(args: Array<String>) = runBlocking {
    log("main : start")
    val v1 = async(CoroutineName("v1coroutine")) {
        delay(500L)
        log("v1")
        252
    }
    val v2 = async(CoroutineName("v2coroutine")) {
        delay(1000L)
        log("v2")
        6
    }
    log("The answer for v1 / v2 = ${v1.await() + v2.await()}")
    log("main : end")
}