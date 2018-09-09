package com.github.mag0716.kotlink

import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    println("My job is ${kotlin.coroutines.coroutineContext[Job]}")
}