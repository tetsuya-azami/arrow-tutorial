package try_catch_vs_either

import arrow.core.*

fun main() {

    when (val it = createPerson("", -1)) {
        is Validated.Valid -> {
            println("personの生成に成功しました。")
            println(it.value)
        }

        is Validated.Invalid -> {
            println("personの生成に失敗しました。")
            println(it.value.joinToString(separator = ",") { it.message })
        }
    }

    when (val it = createPerson("Alice", -1)) {
        is Validated.Valid -> {
            println("personの生成に成功しました。")
            println(it.value)
        }

        is Validated.Invalid -> {
            println("personの生成に失敗しました。")
            println(it.value.joinToString(separator = ",") { it.message })
        }
    }


    when (val it = createPerson("Ted", 20)) {
        is Validated.Valid -> {
            println("personの生成に成功しました。")
            println(it.value)
        }

        is Validated.Invalid -> {
            println("personの生成に失敗しました。")
            println(it.value.joinToString(separator = ",") { it.message })
        }
    }
}

data class ValidationError(val message: String)

data class Person(val name: Name, val age: Age)

fun createPerson(name: String, age: Int): ValidatedNel<ValidationError, Person> {
// Eitherでの実装
// こちらの実装だと複数のValidationErrorをまとめて返せない
//    val validatedName = when (val it = Name.create(name)) {
//        is Either.Right -> it.value.valid()
//        is Either.Left -> return it.
//    }
//
//    val validatedAge = when (val it = Age.create(age)) {
//        is Either.Right -> it.value
//        is Either.Left -> return it
//    }
//
//    return Person(validatedName, validatedAge).right()

    // ValidatedNelでの実装
    // zipを使用して複数のValidatedNel型を返すメソッドを繋げることができる
    // ValidationErrorをまとめて返せる
    return Name.create(name).zip(Age.create(age)) { validatedName, validatedAge ->
        Person(validatedName, validatedAge)
    }
}

sealed class Name private constructor() {
    abstract val value: String

    companion object {
        fun create(name: String): ValidatedNel<ValidationError, Name> {
            if (name.isEmpty() || name.isBlank()) {
                return ValidationError("名前を入力してください。").invalidNel()
            }
            return NameData(name).valid()
        }
    }

    private data class NameData(override val value: String) : Name()
}

sealed class Age private constructor() {
    abstract val value: Int

    companion object {
        fun create(age: Int): ValidatedNel<ValidationError, Age> {
            if (age < 0 || 150 < age) {
                return ValidationError("年齢は0才から150才までです。").invalidNel()
            }
            return AgeData(age).valid()
        }
    }

    data class AgeData(override val value: Int) : Age()
}
