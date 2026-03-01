package com.projach.videogametracker.domain

sealed interface DataResult<out T> {
    class Success<T>(val data: T) : DataResult<T>
    data object Error : DataResult<Nothing>
}

inline fun <T> DataResult<T>.onSuccess(crossinline block: (T?) -> Unit): DataResult<T> =
    when (this) {
        is DataResult.Success -> apply { block(data) }
        is DataResult.Error -> this
    }

inline fun <T> DataResult<T>.onError(crossinline block: DataResult.Error.() -> Unit): DataResult<T> =
    when (this) {
        is DataResult.Error -> apply { this.block() }
        is DataResult.Success -> this
    }

fun <T> DataResult<T>.getOrNull(): T? = when(this){
    is DataResult.Success -> data
    is DataResult.Error -> null
}

fun <T> T.asSuccess() = DataResult.Success(this)

inline fun <T, R> DataResult<T>.mapSuccess(crossinline successMapper: T.() -> R): DataResult<R> = when(this) {
    is DataResult.Success -> data.successMapper().run { DataResult.Success(this) }
    is DataResult.Error -> this
}