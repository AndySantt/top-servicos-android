package com.top.example.ads

import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.coroutines.delay

@Composable
fun rememberAdTimer(
    enabled: Boolean,
    intervalMillis: Long,
    onTrigger: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(enabled) {
        if (!enabled) return@LaunchedEffect

        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                delay(intervalMillis)
                onTrigger()
            }
        }
    }
}

