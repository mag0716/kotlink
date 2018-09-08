package com.github.mag0716.kotlink

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlin.coroutines.CoroutineContext

private fun fizz(context: CoroutineContext) = produce<String>(context) {
    while(true) {
        delay(300)
        send("Fizz")
    }
}

private fun buzz(context: CoroutineContext) = produce<String>(context) {
    while(true) {
        delay(500)
        send("Buzz!")
    }
}

private suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
    select<Unit> {
        fizz.onReceive { value ->
            println("fizz -> '$value'")
        }
        buzz.onReceive { value ->
            println("buzz -> '$value'")
        }
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val fizz = fizz(kotlin.coroutines.coroutineContext)
    val buzz = buzz(kotlin.coroutines.coroutineContext)
    repeat(7) {
        selectFizzBuzz(fizz, buzz)
    }
    coroutineContext.cancelChildren()
}