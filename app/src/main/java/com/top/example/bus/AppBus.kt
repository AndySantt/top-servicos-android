
package com.top.example.bus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppBus {
    private val _events = MutableSharedFlow<Any>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()
    fun emit(ev: Any) { _events.tryEmit(ev) }
}

sealed interface ChatEvent {
    data class Incoming(val convId: String): ChatEvent
}
