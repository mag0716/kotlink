package com.github.mag0716.kotlink

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext

private suspend fun sendString(channel : SendChannel<String>, s : String, time : Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    // 複数のコルーチンから channel に送信される
    val channel = Channel<String>()
    launch(coroutineContext) {
        sendString(channel, "foo", 200L)
    }
    launch(coroutineContext) {
        sendString(channel, "BAR!", 500L)
    }
    repeat(6) {
        println(channel.receive())
    }
    coroutineContext.cancelChildren()
}