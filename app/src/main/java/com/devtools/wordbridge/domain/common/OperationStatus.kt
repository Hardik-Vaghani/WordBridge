package com.devtools.wordbridge.domain.common

sealed class OperationStatus<out T> {
    data class Success<out T>(val data: T? = null, val message: String? = null) : OperationStatus<T>()
    data class Failed<out T>(val data: T? = null, val message: String) : OperationStatus<T>()
    data class Ongoing(val message: String = "Processing...") : OperationStatus<Nothing>()
}
