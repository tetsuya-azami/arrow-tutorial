package try_catch_vs_either

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlin.math.abs
import kotlin.random.Random

fun main() {
    when (val result = apiCall()) {
        is Either.Right -> println(result.value.message)
        is Either.Left -> when (val it = result.value) {
            is ApiCallError.NotFound -> println(it.message)
            is ApiCallError.ServerError -> println("予期しないエラーが発生しました。")
        }
    }
}

fun apiCall(): Either<ApiCallError, ApiCallSuccess> {
    return when (abs(Random.nextInt(3))) {
        0 -> ApiCallSuccess("ApiCallに成功しました。").right()
        1 -> ApiCallError.NotFound("対象のサーバーが見つかりませんでした。").left()
        else -> ApiCallError.ServerError.left()
    }
}

data class ApiCallSuccess(val message: String)

sealed interface ApiCallError {
    data class NotFound(val message: String) : ApiCallError
    data object ServerError : ApiCallError
}