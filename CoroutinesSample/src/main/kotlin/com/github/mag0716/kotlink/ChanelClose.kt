package com.github.mag0716.kotlink

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val channel = Channel<Int>()
    launch {
        for(x in 1..5) channel.send(x * x)
        channel.close()
    }
    // send する側の実装を意識することなく、receive することができる
    for(y in channel) println(y)
    println("main end")
}