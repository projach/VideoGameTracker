package com.projach.videogametracker.data.source.remote

import com.projach.videogametracker.domain.DataResult
import com.projach.videogametracker.domain.asSuccess
import retrofit2.Response

inline fun <T> apiCallAsDataResult(block: () -> Response<T>): DataResult<T> {
    val response = block()
    return if(response.isSuccessful) response.body()?.asSuccess() ?: DataResult.Error
    else DataResult.Error
}