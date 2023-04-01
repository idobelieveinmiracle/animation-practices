package com.rose.animationpractices.view_model.state

sealed class UiState<T> {
    class Success<T>(val data: T) : UiState<T>()
    class Loading<T> : UiState<T>()
    class Idle<T> : UiState<T>()
    class Failure<T>(val messageResId: Int) : UiState<T>()

    fun loading(): Boolean {
        return this is Loading
    }
}