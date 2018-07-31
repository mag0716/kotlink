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

### Waiting for a job

* 他の coroutine の実行待ちに `delay` を使うのはよい方法ではない
* `Job#join`
  * 完了するまで待機する
  * suspend 関数なので、coroutine 内から呼び出す必要がある

### Extract function refactoring

* coroutine 内の処理を別の関数に切り出す
  * 切り出した関数に `suspend` をつける必要がある
  * `suspend` をつければ、`delay` の様な中断関数を呼び出すことができる

### Coroutines ARE light-weight

* 大量の coroutine を起動
  * Thread と異なり OutOfMemory にならず、実行することができる

### Coroutines are like daemon threads

* アクティブな coroutine はプロセスを生かし続けるわけではない
* デーモンスレッドの様に coroutine が終了すると、他の coroutine も終了する

## Cancellation and timeouts
