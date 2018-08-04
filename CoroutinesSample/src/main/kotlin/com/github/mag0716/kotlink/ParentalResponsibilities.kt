package com.github.mag0716.kotlink

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val request = launch {
        repeat(3) { i ->
            // 子の coroutine を join しなくても、処理が実行される
            launch(kotlin.coroutines.coroutineContext) {
                delay((i + 1) * 200L)
                println("coroutine $i end")
            }
        }
        println("request : end")
    }
    request.join()
    println("main : end")
}