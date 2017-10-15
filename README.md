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

## 無名関数

* https://kotlinlang.org/docs/reference/lambdas.html#anonymous-functions
* `()` 内に渡す必要があり、ラムダ式とは異なり、`{}` 内に実装することはできない
* ラムダ式との違いは非ローカルリターンができないこと

## コンストラクタ

* https://kotlinlang.org/docs/reference/classes.html#constructors
* コンストラクタに可視性修飾子やアノテーションをつけない場合は `constructor` は省略可能
* 引数に `val`, `var` をつけるとプロパティとして扱える

### セカンダリコンストラクタ

* `constructor` で定義する
* プライマリコンストラクタを呼び出す場合は `this` を使用する
* セカンダリコンストラクタでは、`val`, `var` の指定ができない

## 継承

* https://kotlinlang.org/docs/reference/classes.html#inheritance
* 継承を可能にするためには親クラスに `open` を指定する必要がある
* デフォルトでは継承不可(Java での `final` が付いた状態)

### オーバライド

* 親クラスのメソッドに `open` を指定する
* 子クラスでは `override` を指定してオーバライドする
* プロパティもオーバライドできる
  * `val` のプロパティを `var` としてオーバライド可能
* 同じ名前のメソッドを定義している複数のクラス、インタフェースを継承した場合は、`super<ClassName>.method()` で呼び出し先を指定できる

## インタフェース

* https://kotlinlang.org/docs/reference/interfaces.html
* `interface`
* 実装を持ったメソッドを定義することができる
* バッキングフィールドを持つことはできない

## クラスデリゲーション

* https://kotlinlang.org/docs/reference/delegation.html
* コンストラクタで実装を渡し、インタフェース部分に `by` で引数で渡した名前を指定する
* この様に実装する事で override したい部分だけ実装して、あとはコンストラクタで渡したクラスに委譲することができる
* プライマリコンストラクタでしか利用できない

## 委譲プロパティ

* https://kotlinlang.org/docs/reference/delegated-properties.html
* プロパティ定義の型の後に `by` でオブジェクトを指定する
* 渡したオブジェクトは `getValue`, `setValue` を実装する必要がある
* [`Delegates`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-delegates/) に委譲プロパティを生成するメソッドがデフォルトで用意されている

## ★ジェネリクス

* https://kotlinlang.org/docs/reference/generics.html
* 同じ型パラメータに複数の制限を持たせる場合は `where` を利用する

### 変位指定

* 不変
  * デフォルト
* 共変
  * `out`
* 反変
  * `in`

#### 参考

* http://kotlin.hatenablog.jp/entry/2012/12/17/234300

### スター投影

* https://kotlinlang.org/docs/reference/generics.html#star-projections
* `*`
* `in Nothing` や `out Any?` のシンタックスシュガー 

### 具象型

* https://kotlinlang.org/docs/reference/inline-functions.html#reified-type-parameters_
* `reified`
* 必ずインライン関数で利用する

#### 参考

* http://sys1yagi.hatenablog.com/entry/2016/05/18/170745

## 安全キャスト

* https://kotlinlang.org/docs/reference/typecasts.html#safe-nullable-cast-operator
* `as?`
* キャストに失敗したら `null` が返却される

### safeCast

* https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect.full/safe-cast.html

## 演算子オーバーロード

* https://kotlinlang.org/docs/reference/operator-overloading.html

### `invoke`

* `invoke` をオーバーロードするとオブジェクト自体が関数の様に呼び出すことができようになる

## 分解宣言

*  https://kotlinlang.org/docs/reference/multi-declarations.html
* `componentN` という関数を持っているオブジェクトオブジェクトで利用できる
* for ループなどでも利用できる
    * `for ((a, b) in collection) { ... }`
* 利用しない値がある場合は、`_` を定義する
