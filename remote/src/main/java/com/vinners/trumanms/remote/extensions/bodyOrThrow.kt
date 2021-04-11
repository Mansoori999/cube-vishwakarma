package com.vinners.trumanms.remote.extensions

import retrofit2.HttpException
import retrofit2.Response

fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) throw HttpException(this)
    return body()!!
}