package com.github.mag0716.kotlink

import kotlinx.coroutines.CommonPool
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

// shared mutable state
private var counter = AtomicInteger()

fun main(args: Array<String>) = runBlocking<Unit> {
    // 1000 * 1000 になる
    massiveRun(CommonPool) {
        counter.incrementAndGet()
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