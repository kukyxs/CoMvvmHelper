@file:Suppress("DEPRECATION")

package com.kk.android.comvvmhelper.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author kuky.
 * @description CoroutineExtensions
 */
inline fun Fragment.launchAndRepeatOnLifecycle(
    context: CoroutineContext = EmptyCoroutineContext,
    owner: LifecycleOwner = viewLifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    owner.lifecycleScope.launch(context) {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            block()
        }
    }
}

inline fun AppCompatActivity.launchAndRepeatOnLifecycle(
    context: CoroutineContext = EmptyCoroutineContext,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(context) {
        repeatOnLifecycle(state) { block() }
    }
}

inline fun CoroutineScope.covLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline onError: suspend CoroutineScope.(CoroutineContext, Throwable) -> Unit = { _, _ -> },
    crossinline onRun: suspend CoroutineScope.() -> Unit
): Job {
    return launch(
        CoroutineExceptionHandler { coroutineContext, throwable ->
            launch(context) { onError(coroutineContext, throwable) }
        } + context
    ) { supervisorScope { onRun() } }
}

/**
 * Simply withContext
 */
suspend fun <T> workOnMain(block: suspend CoroutineScope.() -> T) {
    withContext(Dispatchers.Main) { block() }
}

suspend fun <T> workOnIO(block: suspend CoroutineScope.() -> T) {
    withContext(Dispatchers.IO) { block() }
}

/**
 * Extension for delay actions by coroutine
 */
inline fun CoroutineScope.delayLaunch(
    timeMills: Long, context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    check(timeMills >= 0) { "timeMills must be positive" }
    return launch(context) {
        delay(timeMills)
        block()
    }
}

/**
 * @param interval task interval
 * @param repeatCount task repeat count
 * @param delayTime task star by delayed
 * Extension for repeat task
 */
inline fun CoroutineScope.repeatLaunch(
    interval: Long, repeatCount: Int = Int.MAX_VALUE, delayTime: Long = 0L,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.(Int) -> Unit,
): Job {
    check(interval > 0) { "timeDelta must be large than 0" }
    check(repeatCount > 0) { "repeat count must be large than 0" }

    return launch(context) {
        if (delayTime > 0) delay(delayTime)

        repeat(repeatCount) {
            block(it)
            delay(interval)
        }
    }
}

/////////////////////////////////////////////
/////////////// Deprecated //////////////////
/////////////////////////////////////////////
@Deprecated("replaced by covLaunch", replaceWith = ReplaceWith("replaced by covLaunch"))
data class CoroutineCallback(
    var initDispatcher: CoroutineDispatcher? = null,
    var block: suspend () -> Unit = {},
    var onError: (Throwable) -> Unit = {}
)

/**
 * DSL for handle CoroutineScope throwable
 */
@Deprecated("replaced by covLaunch", replaceWith = ReplaceWith("replaced by covLaunch"))
fun CoroutineScope.safeLaunch(init: CoroutineCallback.() -> Unit): Job {
    val callback = CoroutineCallback().apply { init() }
    return launch(CoroutineExceptionHandler { _, throwable ->
        callback.onError(throwable)
    } + (callback.initDispatcher ?: EmptyCoroutineContext)) {
        callback.block()
    }
}