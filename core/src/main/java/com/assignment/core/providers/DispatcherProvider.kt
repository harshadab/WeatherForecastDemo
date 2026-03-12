package com.assignment.core.providers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val mainDispatcher: CoroutineDispatcher
    val ioDispatcher: CoroutineDispatcher
    val defaultDispatcher : CoroutineDispatcher
    val unconfinedDispatcher: CoroutineDispatcher
}
