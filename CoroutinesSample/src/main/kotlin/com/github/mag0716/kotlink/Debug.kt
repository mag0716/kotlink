package com.github.mag0716.kotlink

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

// -Dkotlinx.courintes.debug をつけないと main だけ
// つけると、@coroutine#1 みたいな出力が追加される
fun main(args: Array<String>) = runBlocking {
    val a = async(kotlin.coroutines.coroutineContext) {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async(coroutineContext) {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}");
}