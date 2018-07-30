# reference memo

* https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md
* https://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html

## Coroutine basics

### Your first coroutine

* `launch` で coroutine builder を開始する
* Thread で置き換える場合は
  * `launch` -> `thread`
  * `delay` -> `Thread.sleep`
* `delay` などの suspend 関数を coroutine 外から呼び出すとコンパイルエラーになる

### Bridging blocking and non-blocking worlds

* `runBlocking`
  * non-blocking な `delay` のみを使って実装することができる
* `runBlocking<Unit> { }` で main coroutine の開始として利用することができる
  * suspend 関数をテストするために利用することができる
