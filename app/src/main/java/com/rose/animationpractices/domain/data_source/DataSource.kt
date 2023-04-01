package com.rose.animationpractices.domain.data_source

sealed class DataSource<T>(val data: T?) {
    class Loading<T>(data: T? = null) : DataSource<T>(data)
    class Success<T>(data: T) : DataSource<T>(data)
    class Failure<T>(data: T? = null, val exception: Exception) : DataSource<T>(data)

    val successDataOrNull: T? get() = when (this) {
        is Success -> data
        else -> null
    }
}