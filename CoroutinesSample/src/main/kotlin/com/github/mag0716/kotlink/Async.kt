package com.github.mag0716.kotlink

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        // doSomethingUsefulOne と doSomethingUsefulTwo が並列で実行され、処理自体は await 前に開始される
        println("before async")
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("after async")
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

private suspend fun doSomethingUsefulOne() : Int {
    println("doSomethingUsefulOne : start")
    delay(1000L)
    println("doSomethingUsefulOne : emd")
    return 13
}

private suspend fun doSomethingUsefulTwo() : Int {
    println("doSomethingUsefulTwo : start")
    delay(1000L)
    println("doSomethingUsefulTwo : end")
    return 29
}