package com.stefang.modern.swapi.core.data

sealed class ResponseModel<T>(
    val message: String
) {

    class Success<T>(val data: T) : ResponseModel<T>("")

    class Error<T>(message: String) : ResponseModel<T>(message)

    class Loading<T> : ResponseModel<T>("")

    class Empty<T>(message: String) : ResponseModel<T>(message)
}
