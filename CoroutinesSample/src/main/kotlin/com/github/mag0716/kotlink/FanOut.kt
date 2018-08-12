package com.github.mag0716.kotlink

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private fun produceNumbers() = produce {
    var x = 1
    while(true) {
        send(x++)
        delay(100L)
    }
}

private fun launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for(msg in channel) {
        println("Processor #$id received $msg")
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val producer = produceNumbers()
    // 5つのプロセッサーを起動するが、produceNumber 内で参照している x は共通で利用されている
    repeat(5) {
        launchProcessor(it, producer)
    }
    delay(950)
    producer.cancel()
}