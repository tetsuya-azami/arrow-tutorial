package try_catch_vs_either

import arrow.core.Either
import arrow.core.left
import arrow.core.right

fun main() {
    val a = readlnOrNull()?.toInt()!!
    val b = readlnOrNull()?.toInt()!!

    // not refactored
//    try {
//        val result = divide(b, a)
//        println(result)
//    }catch (e: IllegalArgumentException){
//        println(e.message)
//    }

    // arrow used
    when (val result = divideUseArrow(b, a)) {
        is Either.Right -> println(result.value)
        is Either.Left -> println(result.value)
    }
}

/**
 * メソッドシグネチャに記述していない挙動が起こる（例外が起こる）。
 * 送出される例外が非検査例外の場合、try-catchでcatchすることを忘れる可能性がある。
 */
private fun divide(b: Int, a: Int): Int {
    if (b == 0) {
        throw IllegalArgumentException("除数を 0 にしてはいけません")
    }
    Result
    return a / b
}

/**
 *
 */
private fun divideUseArrow(b: Int, a: Int): Either<String, Int> {
    return when (b == 0) {
        true -> "除数を 0 にしてはいけません".left()
        false -> (a / b).right()
    }
}