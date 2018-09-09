package com.github.mag0716.kotlink

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlin.coroutines.CoroutineContext

private fun produceNumbers(context: CoroutineContext, side: SendChannel<Int>) = produce<Int>(context) {
    for(num in 1..10) {
        delay(100)
        select<Unit> {
            onSend(num) {}
            side.onSend(num) {}
        }
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val side = Channel<Int>()
    launch(kotlin.coroutines.coroutineContext){
        side.consumeEach { println("Side channel has $it") }
    }
    produceNumbers(kotlin.coroutines.coroutineContext, side).consumeEach {
        // ここで消費しきれなかった場合に side の処理が実行される
        println("Consuming $it")
        delay(250)
    }
    println("Done consuming")
    kotlin.coroutines.coroutineContext.cancelChildren()
}