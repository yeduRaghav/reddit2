package com.yrgv.reddit2.utils

/**
 * Monad implementation that allows either of the two element to be present.
 * Useful when we need a type to represent one of two types to be available at runtime.
 * Based on :
 * https://www.tutorialkart.com/blog/either-monad-design-pattern-and-automatic-function-memoization-in-kotlin/
 */
sealed class Either<out E, out V> {
    data class Error<out L>(val error: L) : Either<L, Nothing>()
    data class Value<out R>(val value: R) : Either<Nothing, R>()

    companion object {
        fun <R> value(value: R): Either<Nothing, R> = Value(value)
        fun <L> error(error: L): Either<L, Nothing> = Error(error)
    }

    fun isError(): Boolean {
        return this is Error
    }

    fun isValue(): Boolean {
        return this is Value
    }

    fun getValueOrNull(): V? {
        return when (this) {
            is Value -> value
            else -> null
        }
    }

    fun getErrorOrNull(): E? {
        return when (this) {
            is Error -> error
            else -> null
        }
    }

}