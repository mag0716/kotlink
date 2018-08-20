package com.github.mag0716.kotlink

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext

fun main(args: Array<String>) = runBlocking<Unit> {
    // receive されていなくても、1 + capacity 分だけ send できる
    val channel = Channel<Int>(4)
    val sender = launch(coroutineContext) {
        repeat(10) {
            println("Sending $it")
            channel.send(it)
        }
    }
    delay(1000L)
    sender.cancel()
}