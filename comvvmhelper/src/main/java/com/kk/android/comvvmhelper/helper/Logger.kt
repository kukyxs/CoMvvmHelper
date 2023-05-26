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
    val className: String get() = javaClass.simpleName
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

private fun KLogger.createLog(message: Any, listLine: Boolean): String {
    return if (listLine) "($fileName:$lineNumber): $message" else "$className: $message"
}

private inline fun KLogger.kLog(
    logger: KLogger, message: Any?,
    throwable: Throwable?,
    func: (String, String) -> Unit,
    funcThrow: (String, String, Throwable) -> Unit,
) {
    val tag = logger.loggerTag
    if (isDebugMode) {
        if (throwable != null)
            funcThrow(tag, createLog(message.toString(), false), throwable)
        else
            func(tag, createLog(message.toString(), false))
    }
}

private fun KLogger.vPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    kLog(
        this, message, thr,
        { tag, msg -> Log.v(tag, msg) },
        { tag, msg, err -> Log.v(tag, msg, err) }
    )
}

private fun KLogger.dPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    kLog(
        this, message, thr,
        { tag, msg -> Log.d(tag, msg) },
        { tag, msg, err -> Log.d(tag, msg, err) }
    )
}

private fun KLogger.iPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    kLog(
        this, message, thr,
        { tag, msg -> Log.i(tag, msg) },
        { tag, msg, err -> Log.i(tag, msg, err) }
    )
}

private fun KLogger.wPrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    kLog(
        this, message, thr,
        { tag, msg -> Log.w(tag, msg) },
        { tag, msg, err -> Log.w(tag, msg, err) }
    )
}

private fun KLogger.ePrint(message: Any?, thr: Throwable? = null) {
    if (!isDebugMode) return
    kLog(
        this, message, thr,
        { tag, msg -> Log.e(tag, msg) },
        { tag, msg, err -> Log.e(tag, msg, err) }
    )
}

////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
fun KLogger.vPrint(listLine: Boolean = false, message: () -> Any?) {
    if (!isDebugMode) return
    if (listLine) getMethodInfo(Throwable())
    Log.v(loggerTag, createLog(message().toString(), listLine))
}

fun KLogger.dPrint(listLine: Boolean = false, message: () -> Any?) {
    if (!isDebugMode) return
    if (listLine) getMethodInfo(Throwable())
    Log.d(loggerTag, createLog(message().toString(), listLine))
}

fun KLogger.iPrint(listLine: Boolean = false, message: () -> Any?) {
    if (!isDebugMode) return
    if (listLine) getMethodInfo(Throwable())
    Log.i(loggerTag, createLog(message().toString(), listLine))
}

fun KLogger.wPrint(listLine: Boolean = false, message: () -> Any?) {
    if (!isDebugMode) return
    if (listLine) getMethodInfo(Throwable())
    Log.w(loggerTag, createLog(message().toString(), listLine))
}

fun KLogger.ePrint(listLine: Boolean = false, message: () -> Any?) {
    if (!isDebugMode) return
    if (listLine) getMethodInfo(Throwable())
    Log.e(loggerTag, createLog(message().toString(), listLine))
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
        vPrint(false) { msg(mes, separator) }
    }
}

fun KLogger.dPrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        dPrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        dPrint(false) { msg(mes, separator) }
    }
}

fun KLogger.iPrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        iPrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        iPrint(false) { msg(mes, separator) }
    }
}

fun KLogger.wPrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        wPrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        wPrint(false) { msg(mes, separator) }
    }
}

fun KLogger.ePrint(vararg message: Any?, separator: String = " ") {
    val mes = message.map { it.covert2String() }
    if (containThrowable(message)) {
        ePrint(msg(mes.dropLast(1), separator), message.last() as Throwable)
    } else {
        ePrint(false) { msg(mes, separator) }
    }
}

sealed class KLogLevel {
    object E : KLogLevel()
    object W : KLogLevel()
    object I : KLogLevel()
    object D : KLogLevel()
    object V : KLogLevel()
}

fun KLogger.logs(vararg message: Any?, separator: String = " ", level: KLogLevel = KLogLevel.E) {
    when (level) {
        KLogLevel.D -> dPrint(*message, separator = separator)
        KLogLevel.E -> ePrint(*message, separator = separator)
        KLogLevel.I -> iPrint(*message, separator = separator)
        KLogLevel.V -> vPrint(*message, separator = separator)
        KLogLevel.W -> wPrint(*message, separator = separator)
    }
}