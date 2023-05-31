package com.kk.android.comvvmhelper.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
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

inline fun Fragment.launchForShared(
    context: CoroutineContext = EmptyCoroutineContext,
    owner: LifecycleOwner = viewLifecycleOwner,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    owner.lifecycleScope.launch(context) { block() }
}

inline fun AppCompatActivity.launchForShared(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(context) { block() }
}

inline fun CoroutineScope.covLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline onError: CoroutineScope.(CoroutineContext, Throwable) -> Unit = { _, _ -> },
    crossinline onRun: suspend CoroutineScope.() -> Unit
): Job {
    return launch(
        CoroutineExceptionHandler { coroutineContext, throwable ->
            onError(coroutineContext, throwable)
        } + context
    ) { supervisorScope { onRun() } }
}

/**
 * Simply withContext
 */
suspend fun <T> withUI(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Main) { block() }
}

suspend fun <T> withIO(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.IO) { block() }
}

@Deprecated("call withUI instead", replaceWith = ReplaceWith("call widthUI instead"))
suspend fun <T> workOnMain(block: suspend CoroutineScope.() -> T) {
    withContext(Dispatchers.Main) { block() }
}

@Deprecated("call withIO instead", replaceWith = ReplaceWith("call withIO instead"))
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