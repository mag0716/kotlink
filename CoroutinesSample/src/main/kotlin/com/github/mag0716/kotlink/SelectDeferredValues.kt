package com.github.mag0716.kotlink

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import java.util.*

private fun asyncString(time: Int) = async {
    delay(time.toLong())
    "Waited for $time ms"
}

private fun asyncStringList(): List<Deferred<String>> {
    val random = Random(3)
    return List(12) {
        val time = random.nextInt(1000)
        println("crete Deferred List : $it, $time")
        asyncString(time)
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val list = asyncStringList()
    val result = select<String> {
        list.withIndex().forEach { (index, deferred) ->
            // ランダムな値によって delay する
            // 一番最初に値が返ってくるのが index = 4
            deferred.onAwait { answer ->
                "Deferred $index produced answer '$answer'"
            }
        }
    }
    println(result)
    val countActive = list.count{ it.isActive }
    println("$countActive coroutines are still active")
}