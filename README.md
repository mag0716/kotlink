# kotlink
Kotlin's memo

## 開発環境

* [Try Kotlin](https://try.kotlinlang.org/#/Examples/Hello,%20world!/Simplest%20version/Simplest%20version.kt)

## Official

* https://github.com/JetBrains/kotlin
* https://kotlinlang.org/docs/reference/
* https://kotlin.link/x

## Coding Style

* https://kotlinlang.org/docs/reference/coding-conventions.html
* https://github.com/raywenderlich/kotlin-style-guide
* https://github.com/openfresh/android-kotlin-style-guide
* https://engineering.vokal.io/Android/KOTLIN.md.html

## Best Practice

* https://blog.philipphauer.de/idiomatic-kotlin-best-practices/

## デコンパイル

* https://sys1yagi.gitbooks.io/anatomy-kotlin/content/

## Memo

### to

* `Pair` を生成する
* `Map` などで利用できる
* https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/to.html
* ex:`"one" to 1`

### `!!`

* https://android.jlelse.eu/how-to-remove-all-from-your-kotlin-code-87dc2c9767fb

### 可変長引数

* [`vararg`](https://kotlinlang.org/docs/reference/functions.html#variable-number-of-arguments-varargs)
* Java と違って一番最後の引数に指定しなくてもよい
  * ただし、その場合は、vararg で指定した引数よりあとの引数には名前付きで指定する必要がある
  * もしくは、後続の引数が関数型であればラムダ式で渡す
* 可変長引数に配列を渡す場合は、`*` を指定する
    * `sum(*intArrayOf(1, 2, 3)`
    
### infix

* https://kotlinlang.org/docs/reference/functions.html#infix-notation
* 引数が1つの関数、拡張関数に `infix` をつけることで以下の様な呼び出しができるようになる
    * `1.shl(2)` -> `1 shl 2`
    
### Tail Call Optimization

* 関数の最後の計算を再帰呼び出しにしてスタックオーバーフローにならないようにする仕組み
* [`tailrec`](https://kotlinlang.org/docs/reference/functions.html#tail-recursive-functions) をつける必要がある
* 内部的にはループ処理に書き換えられる

### ローカル関数

* 関数内に関数を定義できる
* ただし、ローカル関数では `inline` の指定はまだ出来ない
* また、ラベル付き `return` でローカル関数外への `return` もできない
    * https://discuss.kotlinlang.org/t/return-from-outer-function/590/8

## 高階関数

* https://kotlinlang.org/docs/reference/lambdas.html#higher-order-functions
* 引数や戻り値に利用する関数
* `::` を指定して渡すかラムダ式を利用する
* 最後の引数が関数なら`()` の外に `{}` 内にラムダ式を記載できる
  * `lock(lock, { method() })` -> `lock(lock) { method() }`
  
## インライン関数

* https://kotlinlang.org/docs/reference/inline-functions.html
* 引数として渡した関数がコンパイル時に展開される仕組み
* `inline` を指定する
* コードが大きくなるデメリットはあるが、パフォーマンスが向上する
    * ループ処理内で利用している関数で効果を発揮する
* インライン関数内では private な変数やメソッドにはアクセスできない
  * `internal` と `@PublishedApi` の指定が必要

### `noinline`

* インライン関数の引数のラムダを展開させたくない場合に指定する
* ラベル付き `return` は利用できなくなる

### `crossinline`

* インライン関数内で引数のラムダを別の関数で利用する場合に指定する

### 参考

* http://hydrakecat.hatenablog.jp/entry/2015/12/08/Kotlin_Inline_Functions
* https://android.jlelse.eu/inline-noinline-crossinline-what-do-they-mean-b13f48e113c2
