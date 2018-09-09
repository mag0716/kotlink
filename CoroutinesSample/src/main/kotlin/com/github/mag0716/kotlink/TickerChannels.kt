package com.github.mag0716.kotlink

import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

fun main(args: Array<String>) = runBlocking<Unit> {
    val tickerChannel = ticker(delay = 100, initialDelay = 0)
    var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Initial element is available immediately: $nextElement")

    // delay に指定した 100 ms を超えていないので null
    nextElement = withTimeoutOrNull(50) { tickerChannel.receive() }
    println("Next element is not ready in 50 ms: $nextElement")

    // 先ほどの 50 ms と加えて 100ms を超えたので Unit が受け取れる
    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 100 ms: $nextElement")

    println("Consumer pause for 150ms")
    delay(150)
    nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Next element is available immediately after large consumer delay: $nextElement")
    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 50 ms after consumer pause in 150ms: $nextElement")

    tickerChannel.cancel()
}