package com.github.mag0716.kotlink

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

private suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>) : String =
        select<String> {
            // onReceive だと Exception が throw される
            // Exception in thread "main" kotlinx.coroutines.channels.ClosedReceiveChannelException: Channel was closed
            //a.onReceive { value -> "a -> '$value'" }
            //b.onReceive { value -> "b -> '$value'" }
            a.onReceiveOrNull { value ->
                if(value == null) {
                    "Channel 'a' is closed"
                } else {
                    "a -> '$value'"
                }
            }
            b.onReceiveOrNull { value ->
                if(value == null) {
                    "Channel 'b' is closed"
                } else {
                    "b -> '$value'"
                }
            }
        }

fun main(args: Array<String>) = runBlocking<Unit> {
    val a = produce<String>(kotlin.coroutines.coroutineContext) {
        repeat(4) { send("Hello $it")}
    }
    val b = produce<String>(kotlin.coroutines.coroutineContext) {
        repeat(4) { send("World $it") }
    }
    repeat(8) {
        println(selectAorB(a, b))
    }
    coroutineContext.cancelChildren()
}