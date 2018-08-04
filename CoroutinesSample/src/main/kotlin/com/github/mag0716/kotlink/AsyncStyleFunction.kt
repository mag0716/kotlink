package com.github.mag0716.kotlink

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val time = measureTimeMillis {
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()
        runBlocking {
            println("runBlocking start")
            println("The answer ${one.await() + two.await()}")
            println("runBlocking end")
        }
    }
    println("Completed in $time ms")
}

private fun somethingUsefulOneAsync() = async {
    doSomethingUsefulOne()
}

private fun somethingUsefulTwoAsync() = async {
    doSomethingUsefulTwo()
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