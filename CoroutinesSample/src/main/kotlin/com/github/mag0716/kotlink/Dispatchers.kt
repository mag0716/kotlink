package com.github.mag0716.kotlink

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    val jobs = arrayListOf<Job>()
    // main
    jobs += launch(Unconfined) {
        println("Unconfined : ${Thread.currentThread().name}")
    }
    // main
    jobs += launch(kotlin.coroutines.coroutineContext) {
        println("coroutineContext : ${Thread.currentThread().name}")
    }
    // ForkJoinPool.commonPool-worker-1
    jobs += launch(CommonPool) {
        println("CommonPool : ${Thread.currentThread().name}")
    }
    // MyOwner
    jobs += launch(newSingleThreadContext("MyOwner")) {
        println("newSingleThreadContext : ${Thread.currentThread().name}")
    }
    jobs.forEach{ it.join() }
}