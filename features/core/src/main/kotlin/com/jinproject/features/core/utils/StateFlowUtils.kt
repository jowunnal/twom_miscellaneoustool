package com.jinproject.features.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
interface RestartableStateFlow<out T> : StateFlow<T> {
    fun restart()
    fun stopAndStart()
}

interface SharingRestartable : SharingStarted {
    fun restart()
    fun stopAndStart()
}

private data class SharingRestartableImpl(
    private val sharingStarted: SharingStarted,
) : SharingRestartable {

    private val restartFlow = MutableSharedFlow<SharingCommand>(extraBufferCapacity = 2)

    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> {
        return merge(restartFlow, sharingStarted.command(subscriptionCount))
    }

    override fun restart() {
        restartFlow.tryEmit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE)
        restartFlow.tryEmit(SharingCommand.START)
    }

    override fun stopAndStart() {
        restartFlow.tryEmit(SharingCommand.STOP)
        restartFlow.tryEmit(SharingCommand.START)
    }
}

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
fun <T> Flow<T>.restartableStateIn(
    scope: CoroutineScope,
    started: SharingStarted,
    initialValue: T,
): RestartableStateFlow<T> {
    val sharingRestartable = SharingRestartableImpl(started)
    val stateFlow = stateIn(scope, sharingRestartable, initialValue)
    return object : RestartableStateFlow<T>, StateFlow<T> by stateFlow {
        override fun restart() {
            sharingRestartable.restart()
        }

        override fun stopAndStart() {
            sharingRestartable.stopAndStart()
        }
    }
}