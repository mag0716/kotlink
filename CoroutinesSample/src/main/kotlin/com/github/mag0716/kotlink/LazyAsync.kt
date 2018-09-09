package com.github.mag0716.kotlink

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        // doSomethingUsefulOne と doSomethingUsefulTwo が並列で実行され、処理自体は start 後に開始される
        println("before async")
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        println("after async")
        println("before start")
        // start しなくても実行はできるが、await 時に開始されることになり、並列で実行されなくなる
        one.start()
        two.start()
        println("after start")
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