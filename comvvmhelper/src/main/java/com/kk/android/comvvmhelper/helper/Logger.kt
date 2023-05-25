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
            funcThrow(tag, createLog(message.toString()), throwable)
        else
            func(tag, createLog(message.toString()))
    }
}

private fun KLogger.vPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.v(tag, msg) },
        { tag, msg, err -> Log.v(tag, msg, err) })
}

private fun KLogger.dPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.d(tag, msg) },
        { tag, msg, err -> Log.d(tag, msg, err) })
}

private fun KLogger.iPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.i(tag, msg) },
        { tag, msg, err -> Log.i(tag, msg, err) })
}

private fun KLogger.wPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.w(tag, msg) },
        { tag, msg, err -> Log.w(tag, msg, err) })
}

private fun KLogger.ePrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    kLog(this, message, thr,
        { tag, msg -> Log.e(tag, msg) },
        { tag, msg, err -> Log.e(tag, msg, err) })
}

////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
fun KLogger.vPrint(message: () -> Any?) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    Log.v(loggerTag, createLog(message().toString()))
}

fun KLogger.dPrint(message: () -> Any?) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    Log.d(loggerTag, createLog(message().toString()))
}

fun KLogger.iPrint(message: () -> Any?) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    Log.i(loggerTag, createLog(message().toString()))
}

fun KLogger.wPrint(message: () -> Any?) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    Log.w(loggerTag, createLog(message().toString()))
}

fun KLogger.ePrint(message: () -> Any?) {
    if (!isDebugMode) return
    getMethodInfo(Throwable())
    Log.e(loggerTag, createLog(message().toString()))
}

fun KLogger.jsonPrint(errorLevel: Boolean = true, message: () -> String) {
    if (!isDebugMode) return

    val json = message()
    getMethodInfo(Throwable())

    if (json.isBlank()) {
        Log.i(loggerTag, json)
        return
    }

    val jsonInformation = try {
        when {
            json.startsWith("{") && json.endsWith("}") -> JSONObject(json).toString(4)
            json.startsWith("[") && json.endsWith("]") -> JSONArray(json).toString(4)
            else -> json
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

////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
private fun msg(message: List<String>, separator: String = " "): String {
    return message.joinToString(separator)
}

private fun Any?.covert2String(): String {
    return when {
        this == null -> "null"
        this is Number || this is Char || this is String || this is Boolean -> "$this"
        this is List<*> -> joinToString("\n") { it.covert2String() } + "\n"
        this is Array<*> -> joinToString("\n") { it.covert2String() } + "\n"
        this is BooleanArray -> joinToString("\n") { toString() } + "\n"
        this is ByteArray -> joinToString("\n") { toString() } + "\n"
        this is IntArray -> joinToString("\n") { toString() } + "\n"
        this is LongArray -> joinToString("\n") { toString() } + "\n"
        this is ShortArray -> joinToString("\n") { toString() } + "\n"
        this is FloatArray -> joinToString("\n") { toString() } + "\n"
        this is DoubleArray -> joinToString("\n") { toString() } + "\n"
        this is CharArray -> joinToString("\n") { toString() } + "\n"
        else -> toString()
    }
}

private fun containThrowable(vararg message: Any?) = message.size > 1 && message.last() is Throwable

fun KLogger.vPrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        vPrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        vPrint { msg(mes, separator) }
    }
}

fun KLogger.dPrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        dPrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        dPrint { msg(mes, separator) }
    }
}

fun KLogger.iPrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        iPrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        iPrint { msg(mes, separator) }
    }
}

fun KLogger.wPrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        wPrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        wPrint { msg(mes, separator) }
    }
}

fun KLogger.ePrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        ePrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        ePrint { msg(mes, separator) }
    }
}