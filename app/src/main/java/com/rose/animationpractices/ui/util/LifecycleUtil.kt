package com.rose.animationpractices.ui.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> SharedFlow<T>.collectWhenActive(
    lifecycleOwner: LifecycleOwner,
    isCollectLatest: Boolean = true,
    onCollected: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            if (isCollectLatest) {
                collectLatest { data -> onCollected(data) }
            } else {
                collect { data -> onCollected(data) }
            }
        }
    }
}

