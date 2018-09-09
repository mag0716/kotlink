package com.github.mag0716.kotlink

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        // doSomethingUsefulOne -> doSomethingUsefulTwo
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
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