package com.lordrians.ojeksyariapi.utils

import com.lordrians.ojeksyariapi.utils.Result

open class BaseResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T?
){
    companion object{
        fun<T> Success(data: T): BaseResponse<T> = BaseResponse(
            data = data,
            status = true,
            message = "Success"
        )
        fun<T> Error(error: Exception): BaseResponse<T> = BaseResponse(
            false,
            message = "Error: ${error.message}",
            data = null
        )
    }
}


fun <T, S> Result<T>.next(nextFunc:(result: Result<T>) -> Result<S>): Result<S> =
    when (this) {
        is Result.Success -> nextFunc(this)
        is Result.Error -> this
    }




fun <T> tryCatch(
    codeBlock: () -> Result<T>
): Result<T> = try {
    codeBlock.invoke()
} catch (e: Exception) {
    Result.Error(e)
}


sealed class Result<out T>{
    data class Success<S>(val data: S): Result<S>()
    data class Error(val error: Exception): Result<Nothing>()
}

val <T> Result<T>.data : T
    get() = when(this){
        is Result.Success -> this.data
        is Result.Error -> throw error
    }

fun <T> Result<T>.toBaseRes(): BaseResponse<T>{
    return when(this){
        is Result.Success -> BaseResponse.Success(this.data)
        is Result.Error -> BaseResponse.Error(this.error)
    }
}
