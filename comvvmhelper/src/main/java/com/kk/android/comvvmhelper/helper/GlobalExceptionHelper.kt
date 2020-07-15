@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.kk.android.comvvmhelper.helper

import android.content.Context
import android.os.Build
import com.kk.android.comvvmhelper.utils.getAppVersionCode
import com.kk.android.comvvmhelper.utils.getAppVersionName
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author kuky.
 * @description
 */

class GlobalExceptionHelper private constructor(
    private val context: Context,
    private val globalExceptionHandle: (Throwable) -> Unit
) : Thread.UncaughtExceptionHandler {
    companion object {
        @Volatile
        private var INSTANCE: GlobalExceptionHelper? = null

        fun getInstance(
            context: Context,
            exceptionHandler: (Throwable) -> Unit = {}
        ) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: GlobalExceptionHelper(context, exceptionHandler).also { INSTANCE = it }
        }
    }

    private val mExceptionCachePool = hashMapOf<String, String>()

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        collectExceptionMessage()
        storeExceptionMessage(e)
        globalExceptionHandle(e)
    }

    private fun collectExceptionMessage() {
        try {
            mExceptionCachePool["versionName"] = context.getAppVersionName()
            mExceptionCachePool["versionCode"] = "${context.getAppVersionCode()}"

            Build::class.java.fields.forEach { field ->
                field.isAccessible = true
                mExceptionCachePool[field.name] = field.get(null).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun storeExceptionMessage(e: Throwable) {
        val sb = StringBuilder()
        mExceptionCachePool.forEach { entry ->
            sb.append(entry.key).append("=").append(entry.value).append("\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)

        var cause = e.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = e.cause
        }
        printWriter.close()
        sb.append(writer.toString())

        val fileName = "crash_logs/exception_crash-${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())}.log"
        val targetFile = File(context.cacheDir, fileName)

        if (targetFile.parentFile?.exists() == false)
            targetFile.parentFile?.mkdirs()

        if (!targetFile.exists())
            targetFile.createNewFile()

        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(targetFile)
            fos.write(sb.toString().toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
    }
}