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

### Cancelling coroutine execution

* `Job#cancel`
  * キャンセルするとすぐに、他の coroutine の処理は実行されない
  * 拡張関数である `Job#cancelAndJoin` で `cancel` と `join` を実行する

### Cancellation is cooperative

* 中断関数は全てキャンセル可能
* coroutine 内の処理でキャンセルされたかどうかをチェックしない場合は、処理自体を取り消すことはできない

### Making computation code cancellable

* coroutine 内の処理をキャンセルする
  * 定期的に中断をチェックする中断関数を呼び出す
    * `yield`
  * キャンセルステータスを明示的にチェックする
    * `CoroutieScope.isActive`

### Closing resources with finally

* キャンセル可能な中断関数は、`CancellationException` をスローする
  * try - catch - finally の `finally` や `use` の終了処理が実行される
* `Job#join` は終了処理が完了するまで待機する

### Run non-cancellable block

* finally 句で、中断関数を呼び出すこともできる
  * `withContext` と `NonCancellable` を使うことで可能

### Timeout

* `withTimeout` で指定時間を超えたらタイムアウトする coroutine を開始することができる
* タイムアウトになったら `TimeoutCancellationException` が発生する
  * `CancellationException` のサブクラス
  * try - catch でハンドリングすることも可能
  * `withTimeoutOrNull` でタイムアウトしたら null を取得するようにもできる
    * 内部で `suspendCoroutineUninterceptedOrReturn` を使っている

### Composing suspending functions

#### Sequential by default

* 中断関数の実行はデフォルトではシーケンシャルに実行される
* `measureTimeMills`
  * 処理にかかった時間を計測をしてくれる

#### Concurrent using async

* 複数の中断関数をパラレルで実行するには `async` を利用する
  * `async` は `launch` の様に coroutine を開始するが、`launch` と違って結果を取得することができる
    * `Job` でなく `Deferred` が返される
  * `Deferred#await` で結果を取得できる
    * `Job` と同じくキャンセルもできる

#### Lazily started async

* `async` の `start` パラメータに `CoroutineStart.LAZY` を渡すことで開始タイミングを逝去できる
  * `Deferred#start` に開始される
  * `Deferred#await` でも開始されるが、並列での実行ができなくなる

#### Async-style functions

* `async` を使って関数を定義することもできる
  * suffix に Async をつけるのがよい
* 中断関数ではないのでどこからでも呼び出せるが、結果を受け取るには、中断関数である `await` が必要になるので、coroutine は必要になる

### Coroutine context and dispatchers

* coroutine は常に CoroutineContext 型で指定されるコンテキストで実行される
* コンテキストの主な要素は `Job` と `Dispatcher`

#### Dispatchers and threads

* coroutine のコンテキストには `CoroutineDispatcher` が含まれている
* ディスパッチャーは coroutine の実行するスレッドを限定したり、スレッドプールに非同期で実行させたりすることが可能
* `launch` は `async` のような coroutine のビルダーには、ディスパッチャーを指定できるように `CoroutineContext` を指定することができる
* デフォルトでは `DefaultDispatcher` が利用されている
  * サンプルの実装での `CommonPool` と同じ
* `newSingleThreadContext` は新しいスレッドを作成するが、expensive なリソースなので注意が必要
  * 実際のアプリでは `close` で解放するか、アプリ全体で再利用する必要がある

#### Unconfined vs confined dispatcher

* `Unconfined`
  * 中断関数までは coroutine が開始されたスレッドで呼び出されるが、中断関数後は中断関数で決定されたスレッド再開される
  * CPU 時間を消費しない場合や UI などを更新しない場合に適切
* `coroutineContext`
  * `CoroutineScope` を介して、coroutine のブロック内で利用できるプロパティ
  * 親のコンテキストを継承できる
  * `runBlocking` では、呼び出し側スレッドに限定されているため、FIFO でスケジューリングされる

#### Debugging coroutines and threads

* coroutine はスレッドの中断と `Uniconfined`　やデフォルトのディスパッチャーで他のスレッドの再開をすることができる
* 1つのスレッドでさえ、どこで、いつ動作しているのかを図示するのは困難
* スレッドを利用するアプリのデバッグの共通の方法はログを出力すること
* kotlinx.coroutines に coroutine のデバッグを容易にする機能が用意されている
  * `-Dkotlinx.coroutines.debug` をつけて実行すると、どの coroutine で実行が分かりやすくなる
  * coroutine#1 のような追加情報が出力される
  * さらに詳しい情報は [newCoroutineContext](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.experimental/new-coroutine-context.html) を参考にする

#### Jumping between threads

* 1つの coroutine 内で `withContext` を使ってコンテキストを切り替えることができる
  * 実行している coroutine 自体は変わらない

#### Job in the context

* `Job` は コンテキストの一部
* coroutine は `coroutineContext` を使って自身のコンテキストから検索することが可能
* `isActive`
  * `couroutineContext[Job]?.isActive == true`

#### Children of a coroutine

* `coroutineContext` が他の coroutine　の起動に使われるとき、新しい coroutine の `Job` は子供として作成される
  * 親の coroutine がキャンセルされたら、子供の coroutine も再帰的にキャンセルされる

#### Combining contexts

* coroutine のコンテキストは `+` を使って結合することが可能
* `coroutineContext + CommonPool`
  * CommonPool で CPU-intensive な子供の Job を生成する
  * 親のコンテキストを継承するので、親がキャンセルされた時にキャンセルすることができる

#### Parental responsibilities

* 親の coroutine は常に子供の完了を待機する
* 子の coroutine の全てを追う必要はなく、`Job#join` を利用する必要はない

#### Naming coroutines for debugging

* 自動的にアサインされる ID はデバッグには便利
* coroutine が特定の要求の処理やバックグランドタスクの場合は、明示的に名前を指定するとよい
  * `CoroutineName`
  * デバッグモードが有効になっている場合に出力される

#### Cancellation via explicit job

* `launch(coroutineContext, parent = job)`
  * 指定した `Job` をキャンセルすることで、coroutine もキャンセルすることが可能
* Android アプリでは、Activity 生成時に `Job` を生成し、Activity が破棄されたときにキャンセルすればよい

### Channels

* `Deferred` は coroutine 間で1つの値を送信する方法を提供する
* Channels はストリームを送信する方法を提供する

#### Channel basics

* `BlockingQueue` の概念と似ている
  * 違いは、put -> send, take -> receive

#### Closing and iteration over channels

* キューと違って、Channels は明示的に閉じることが可能
  * `close`
  * 概念的には、特別なトークンを Channels に送信する
  * `for(value in channel)`
    * トークンを受け取るとすぐにループが終了する

#### Building channel producers

* coroutine が要素のシーケンスを生成するパターンは Producer-Consumer パターンの一部
  * `produce`, `consumeEach` を使って実装することができる
  * `produce` は coroutine ビルダー

#### Pipelines

* Pipeline は無限の値のストリームを生成するパターン
* coroutine はデーモンスレッドのようなものなのでキャンセルする必要はないが、大きなアプリでは明示的に停止した方がよい

#### Prime numbers with pipeline

* `cancelChildren`
  * すべての子 coroutine をキャンセルする拡張関数
* `builderIterator` で同じような実装ができる
  * `produce` -> `buildIterator`
  * `send` -> `yield`
  * `receive` -> `next`
  * `ReceiveChannel` -> `Iterator`
* `CommonPool` コンテキストで実行すると、複数の CPUコアを利用できる

#### Fan-out

* 複数のコルーチンが同じチャネルから受信し、コルーチンで作業を分散することができる
* プロデューサのコルーチンをキャンセルするとそのチャネルが閉じられ、最終的にプロセッサのコルーチンが行なっているチャネルでの繰り返しが終了する
* Also, pay attention to how we explicitly iterate over channel with for loop to perform fan-out in launchProcessor code. Unlike consumeEach, this for loop pattern is perfectly safe to use from multiple coroutines. If one of the processor coroutines fails, then others would still be processing the channel, while a processor that is written via consumeEach always consumes (cancels) the underlying channel on its normal or abnormal termination.

#### Fan-in

* 複数のコルーチンが同じチャネルに送信することができる

#### Buffered channels

* バッファーされていないチャネルは送信側と受信側がある場合に要素が送信される
  * send が呼ばれた場合、receive まで中断される
  * receive が呼ばれた場合、send まで中断される
* `Channel()` のファクトリー関数と `produce` ビルダーはバッファーサイズをパラメーターで指定することができる
  * バッファーは `BlockingQueue` のように中断前に複数の要素を送信できるようにする
  * バッファーがいっぱいになるとブロックされる

#### ？Ticker channels

* Ticker channel は、このチャネルから消費から一定時間毎に Unit が生成される特別なチャネル
* ウィンドウ処理や時間に依存する処理に便利
* `ReceiveChannel.cancel`
  * それ以上の要素が必要でないことを示す
* `TickerMode.FIXED_DELAY`
  * 要素間の固定の遅延を維持することができる

#### Channels are fair

* チャネルへの send, receive 操作は、複数のコルーチンからの呼び出し順番に関して公平
  * FIFO

### Shared mutable state and concurrency

* コルーチンは、デフォルトの CommonPool のようなマルチスレッド Dispatcher を使用して同時に実行できる
* 主な問題は、共有ミュータブルステートへのアクセスへの同期

#### The problem

* 複数のコルーチンから共有のミュータブルな値を操作すると同期されない
* CPU の数によっては、同期されることもあるので注意

#### Volatiles are of no help

* `@Volatile`
  * 動作が遅くなる上に同期はされない
  * atomic な読み書きを保証するものなので、インクリメントなどには使えない

#### Thread-safe data structures

* スレッドでもコルーチンでも共通な解決方法はスレッドセーフなデータを利用すること
  * 例：`AtomicInteger`
* もっとも簡単な方法だが、複雑なステートなどには利用できない

#### Thread confinement fine-grained

* ステートへのアクセスを1つのスレッドのみで行う
  * `newSingleThreadContext`
* UIアプリケーションなどで使われる方法だが、動作は遅い

#### Thread confinement coarse-grained

* ？In practice, thread confinement is performed in large chunks, e.g. big pieces of state-updating business logic are confined to the single thread.
* コルーチンの起動自体を1つのスレッドで制限するだけで正しく同期されるようになる

#### Mutual exclusion

* `Mutex` を利用することでも解決できる
  * `synchronized` のようなもの
  * `Mutex#lock`, `Mutex#unlock`
    * 中断関数でスレッドをブロックしない
  * `Mutex#withLock` を使えば、 lock と unlock を行なってくれる
* Thread confinement fine-grained と同じで、動作は遅い
* 定期的に変更する必要があるケースに適している
