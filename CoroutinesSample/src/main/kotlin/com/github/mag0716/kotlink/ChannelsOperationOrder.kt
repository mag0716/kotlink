package com.github.mag0716.kotlink

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class Ball(var hits: Int)

fun main(args: Array<String>) = runBlocking<Unit> {
    val table = Channel<Ball>()
    // ping -> pong -> ping ... の順で出力される
    launch(kotlin.coroutines.coroutineContext) { player("ping", table) }
    launch(kotlin.coroutines.coroutineContext) { player("pong", table) }
    table.send(Ball(0))
    delay(1000L)
    kotlin.coroutines.coroutineContext.cancelChildren()
}

private suspend fun player(name: String, table: Channel<Ball>) {
    for(ball in table) {
        ball.hits++
        println("$name $ball")
        delay(300L)
        table.send(ball)
    }
}