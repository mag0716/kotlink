package com.github.mag0716.kotlink

private fun numbersFrom(start: Int) = buildIterator {
    var x = start
    while(true) {
        yield(x++)
        println("yield in numbersFrom : $x")
    }
}

private fun filter(numbers: Iterator<Int>, prime: Int) = buildIterator {
    for(x in numbers) {
        if (x % prime != 0) {
            yield(x)
        }
        println("filter $x, $prime")
    }
}

/*
Exception in thread "main" java.lang.IncompatibleClassChangeError
	at com.github.mag0716.kotlink.PrimeNumberWithBuildIteratorKt$filter$1.invokeSuspend(PrimeNumberWithBuildIterator.kt:18)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:32)
	at kotlin.sequences.SequenceBuilderIterator.hasNext(SequenceBuilder.kt:123)
	at com.github.mag0716.kotlink.PrimeNumberWithBuildIteratorKt$filter$1.invokeSuspend(PrimeNumberWithBuildIterator.kt:18)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:32)
	at kotlin.sequences.SequenceBuilderIterator.hasNext(SequenceBuilder.kt:123)
	at kotlin.sequences.SequenceBuilderIterator.nextNotReady(SequenceBuilder.kt:146)
	at kotlin.sequences.SequenceBuilderIterator.next(SequenceBuilder.kt:129)
	at com.github.mag0716.kotlink.PrimeNumberWithBuildIteratorKt.main(PrimeNumberWithBuildIterator.kt:31)
 */
fun main(args: Array<String>) {
    var cur = numbersFrom(2)
    // 素数を10個出力する
    for(i in 1..10) {
        println("before next")
        val prime = cur.next()
        println("after next")
        println(prime)
        cur = filter(cur, prime)
    }
    println("main end")
}