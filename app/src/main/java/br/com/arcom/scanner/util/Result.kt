package br.com.arcom.scanner.util

sealed class Result {
    object Loading : Result()
    object Ok : Result()
    data class Error(val e: Exception) : Result()
    object Unauthorized : Result()
    object Token : Result()
}
