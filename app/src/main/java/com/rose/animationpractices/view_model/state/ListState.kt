package com.rose.animationpractices.view_model.state

data class ListState<T>(
    val loading: Boolean = false,
    val messageRes: Int = 0,
    val items: List<T> = emptyList()
)
