package com.github.mag0716.kotlink

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

private val counterContext = newSingleThreadContext("counterContext")
// shared mutable state
private var counter = 0

fun main(args: Array<String>) = runBlocking<Unit> {
    massiveRun(CommonPool) {
        // これで同期しているので 1000 * 1000 になるが時間がかかる
        withContext(counterContext) {
            counter++
        }
    }
    println("Counter = $counter")
}

suspend private fun massiveRun(context: CoroutineContext, action: suspend () -> Unit) {
    val n = 1000
    val k = 1000
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch(context) {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")
}