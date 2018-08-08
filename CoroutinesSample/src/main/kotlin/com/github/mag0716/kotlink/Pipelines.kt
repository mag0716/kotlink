package com.github.mag0716.kotlink

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking

private fun produceNumbers() = produce {
    var x = 1
    while (true) {
        send(x++)
        println("send in produceNumbers : $x")
    }
}

private fun square(numbers: ReceiveChannel<Int>) = produce {
    for(x in numbers) {
        send(x * x)
        println("send in square : ${x*x}")
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val numbers = produceNumbers()
    val squares = square(numbers)
    for(i in 1..5) {
        println("before receive : $i")
        println(squares.receive())
        println("after receive : $i")
    }
    println("main end")
    squares.cancel()
    numbers.cancel()
}