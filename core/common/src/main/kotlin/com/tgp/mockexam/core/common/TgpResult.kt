package com.tgp.mockexam.core.common

sealed interface TgpResult<out T> {
    data class Success<T>(val data: T) : TgpResult<T>
    data class Error(val throwable: Throwable) : TgpResult<Nothing>
}