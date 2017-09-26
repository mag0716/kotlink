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
