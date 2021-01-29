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
    fun json(json: String?) {
        if (json.isNullOrBlank()) {
            i(json.toString())
            return
        }

        try {
            getMethodName(Throwable())
            Log.i(
                className, createLog(
                    when {
                        json.startsWith("{") -> JSONObject(json).toString(4)

                        json.startsWith("[") -> JSONArray(json).toString(4)

                        else -> json
                    }
                )
            )
        } catch (e: Exception) {
            error("${e.cause?.message}${System.getProperty("line.separator")}$json")
        }
    }

    fun e(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.e(className, createLog(msg.toString()))
    }

    fun w(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.w(className, createLog(msg.toString()))
    }

    fun i(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.i(className, createLog(msg.toString()))
    }

    fun d(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.d(className, createLog(msg.toString()))
    }

    fun v(msg: Any?) {
        if (!isDebugMode) return
        getMethodName(Throwable())
        Log.v(className, createLog(msg.toString()))
    }
}