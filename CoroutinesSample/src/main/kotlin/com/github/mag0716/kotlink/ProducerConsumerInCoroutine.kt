package com.github.mag0716.kotlink

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking

private fun produceSquares() = produce {
    for(x in 1..5) send(x * x)
}

fun main(args: Array<String>) = runBlocking {
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    println("main end")
}