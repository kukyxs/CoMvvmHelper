package com.kk.android.comvvmhelper.utils

import android.util.Log
import com.kk.android.comvvmhelper.helper.isDebugMode
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author kuky.
 * @description
 */
object LogUtils {
    private const val EMPTY_MESSAGE = "<===== empty message =====>"
    private var className: String? = null
    private var methodName: String? = null
    private var lineNumber: Int? = null

    private fun createLog(logMsg: String): String = "$methodName($className:$lineNumber): $logMsg"

    private fun getMethodName(throwable: Throwable) {
        className = throwable.stackTrace[1].fileName
        methodName = throwable.stackTrace[1].methodName
        lineNumber = throwable.stackTrace[1].lineNumber
    }

    /** format json data */
    fun logJson(json: String?) {
        if (json.isNullOrBlank()) {
            logInfo(EMPTY_MESSAGE)
            return
        }

        try {
            getMethodName(Throwable())
            Log.i(
                className, createLog(
                    when {
                        json.startsWith("{") -> JSONObject(json).toString(4)

                        json.startsWith("[") -> JSONArray(json).toString(4)

                        else -> ""
                    }
                )
            )
        } catch (e: Exception) {
            error("${e.cause?.message}${System.getProperty("line.separator")}$json")
        }
    }

    fun logError(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.e(className, createLog(msg?.toString() ?: EMPTY_MESSAGE))
    }

    fun logWarm(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.w(className, createLog(msg?.toString() ?: EMPTY_MESSAGE))
    }

    fun logInfo(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.i(className, createLog(msg?.toString() ?: EMPTY_MESSAGE))
    }

    fun logDebug(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.d(className, createLog(msg?.toString() ?: EMPTY_MESSAGE))
    }

    fun logVerbose(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.v(className, createLog(msg?.toString() ?: EMPTY_MESSAGE))
    }
}