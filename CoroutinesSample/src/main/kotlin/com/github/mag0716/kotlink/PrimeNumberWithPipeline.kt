package com.github.mag0716.kotlink

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

private fun numbersFrom(context: CoroutineContext, start: Int) = produce(context) {
    var x = start
    while(true) {
        send(x++)
        // 29 よりも大きい値が send されるつづける
        println("send in numbersFrom : $x")
    }
}

private fun filter(context: CoroutineContext, numbers: ReceiveChannel<Int>, prime: Int) = produce(context) {
    for(x in numbers) {
        if (x % prime != 0) {
            send(x)
        }
        println("filter $x, $prime")
    }
}

fun main(args: Array<String>) = runBlocking {
    var cur = numbersFrom(kotlin.coroutines.coroutineContext, 2)
    // 素数を10個出力する
    for(i in 1..10) {
        println("before receive")
        val prime = cur.receive()
        println("after receive")
        println(prime)
        cur = filter(kotlin.coroutines.coroutineContext, cur, prime)
    }
    // キャンセルしないとプロセスが生き残ったままになる
    kotlin.coroutines.coroutineContext.cancelChildren()
    println("main end")
}