package com.github.mag0716.kotlink

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select

private fun switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
    var current = input.receive()
    while(isActive) {
        val next = select<Deferred<String>?> {
            input.onReceiveOrNull { update ->
                println("onReceiveOrNull $update")
                update
            }
            current.onAwait { value ->
                println("onAwait $value")
                send(value)
                input.receiveOrNull()
            }
        }
        if(next == null) {
            println("Channel was closed")
            break
        } else {
            current = next
        }
    }
}

private fun asyncString(str: String, time: Long) = async {
    delay(time)
    str
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val channel = Channel<Deferred<String>>()
    launch(coroutineContext) {
        for(s in switchMapDeferreds(channel)) {
            println(s)
        }
    }
    channel.send(asyncString("BEGIN", 100))
    delay(200)
    // Slow は消費される前に Replace が send されてしまう
    // そうすると、onAwait ではなく、onReceiveOrNull が呼ばれ、current が更新される
    channel.send(asyncString("Slow", 500))
    delay(100)
    channel.send(asyncString("Replace", 100))
    delay(500)
    channel.send(asyncString("END", 500))
    delay(1000)
    channel.close()
    delay(500)
}