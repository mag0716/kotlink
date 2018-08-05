package com.github.mag0716.kotlink

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val channel = Channel<Int>()
    launch {
        for(x in 1..5) channel.send(x * x)
    }
    repeat(5) { println(channel.receive()) }
    println("main end")
}