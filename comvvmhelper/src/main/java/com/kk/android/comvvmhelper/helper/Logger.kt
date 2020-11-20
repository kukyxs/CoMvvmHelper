package com.kk.android.comvvmhelper.helper

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author kuky.
 * @description KLogger usage
 *
 * ```kotlin
 *      class Test : KLogger {
 *          fun test(){
 *              ePrint{ "KLogger go->" } // logger tag is your class name
 *          }
 *      }
 * ```
 *
 * or
 *
 * ```kotlin
 *      class Test {
 *          fun test(){
 *              kLogger<Test>().ePrint{" KLogger go->" } // logger tag is your class name
 *          }
 *      }
 * ```
 *
 * or
 *
 * ```kotlin
 *      class Test{
 *          fun test(){
 *              kLogger.ePrint{"KLogger go->"} // logger tag is KLogger
 *          }
 *      }
 * ```
 */
private var fileName: String = ""
private var lineNumber: Int = -1
internal var isDebugMode = true

val kLogger = kLogger<KLogger>()

interface KLogger {
    val loggerTag: String get() = getTag(javaClass)
}

fun kLogger(clazz: Class<*>): KLogger = object : KLogger {
    override val loggerTag: String get() = getTag(clazz)
}

inline fun <reified T : Any> kLogger(): KLogger = kLogger(T::class.java)

private fun getTag(clazz: Class<*>) = clazz.simpleName.let {
    if (it.length <= 23) it else it.substring(0, 23)
}

private fun getMethodInfo(thr: Throwable) {
    fileName = thr.stackTrace[1]?.fileName ?: ""
    lineNumber = thr.stackTrace[1]?.lineNumber ?: 0
}

private fun createLog(message: Any): String {
    return "($fileName:$lineNumber): $message"
}

private inline fun kLog(
    logger: KLogger, message: Any?,
    throwable: Throwable?,
    func: (String, String) -> Unit,
    funcThrow: (String, String, Throwable) -> Unit
) {
    val tag = logger.loggerTag
    if (isDebugMode) {
        if (throwable != null)
            funcThrow(tag, createLog(message?.toString() ?: "null"), throwable)
        else
            func(tag, createLog(message?.toString() ?: "null"))
    }
}

fun KLogger.vPrint(message: Any?, thr: Throwable? = null) {
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.v(tag, msg) },
        { tag, msg, err -> Log.v(tag, msg, err) })
}

fun KLogger.dPrint(message: Any?, thr: Throwable? = null) {
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.d(tag, msg) },
        { tag, msg, err -> Log.d(tag, msg, err) })
}

fun KLogger.iPrint(message: Any?, thr: Throwable? = null) {
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.i(tag, msg) },
        { tag, msg, err -> Log.i(tag, msg, err) })
}

fun KLogger.wPrint(message: Any?, thr: Throwable? = null) {
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.w(tag, msg) },
        { tag, msg, err -> Log.w(tag, msg, err) })
}

fun KLogger.ePrint(message: Any?, thr: Throwable? = null) {
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.e(tag, msg) },
        { tag, msg, err -> Log.e(tag, msg, err) })
}

// <-----------------------  ------------------------------->
fun KLogger.vPrint(message: () -> Any?) {
    getMethodInfo(Throwable())
    if (isDebugMode)
        Log.v(loggerTag, createLog(message()?.toString() ?: "null"))
}

fun KLogger.dPrint(message: () -> Any?) {
    getMethodInfo(Throwable())
    if (isDebugMode)
        Log.d(loggerTag, createLog(message()?.toString() ?: "null"))
}

fun KLogger.iPrint(message: () -> Any?) {
    getMethodInfo(Throwable())
    if (isDebugMode)
        Log.i(loggerTag, createLog(message()?.toString() ?: "null"))
}

fun KLogger.wPrint(message: () -> Any?) {
    getMethodInfo(Throwable())
    if (isDebugMode)
        Log.w(loggerTag, createLog(message()?.toString() ?: "null"))
}

fun KLogger.ePrint(message: () -> Any?) {
    getMethodInfo(Throwable())
    if (isDebugMode)
        Log.e(loggerTag, createLog(message()?.toString() ?: "null"))
}

fun KLogger.jsonPrint(errorLevel: Boolean = true, message: () -> String) {
    val json = message()
    getMethodInfo(Throwable())

    if (isDebugMode) {
        if (json.isBlank()) {
            return
        }

        val jsonInformation = try {
            when {
                json.startsWith("{") && json.endsWith("}") -> JSONObject(json).toString(4)
                json.startsWith("[") && json.endsWith("]") -> JSONArray(json).toString(4)
                else -> "bad json information: ($json)"
            }
        } catch (e: Exception) {
            "${e.cause?.message}${System.getProperty("line.separator")}: $json"
        }

        if (errorLevel) {
            Log.e(loggerTag, jsonInformation)
        } else {
            Log.i(loggerTag, jsonInformation)
        }
    }
}