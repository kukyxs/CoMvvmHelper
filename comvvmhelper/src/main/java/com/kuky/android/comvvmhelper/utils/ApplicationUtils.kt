@file:Suppress("DEPRECATION")

package com.kuky.android.comvvmhelper.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @author kuky.
 * @description
 */
fun getAppName(context: Context): String = try {
    val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val labelRes = packageInfo.applicationInfo.labelRes
    context.resources.getString(labelRes)
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    ""
}

fun getAppVersionName(context: Context): String = try {
    val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    packageInfo.versionName
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    ""
}

fun getAppVersionCode(context: Context): Long =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) getLongAppVersion(context) else getAppIntVersion(context).toLong()

private fun getAppIntVersion(context: Context): Int = try {
    context.packageManager.getPackageInfo(context.packageName, 0).versionCode
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    0
}

@RequiresApi(Build.VERSION_CODES.P)
private fun getLongAppVersion(context: Context): Long = try {
    context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    0L
}

fun starApp(context: Context, packageName: String, fail: () -> Unit) =
    try {
        context.startActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            component = context.packageManager.getLaunchIntentForPackage(packageName)?.component
        })
    } catch (e: Exception) {
        e.printStackTrace()
        fail()
    }

fun getAppIcon(context: Context, pkgName: String): Bitmap? = context.packageManager.let { pm ->
    try {
        val drawable = pm.getApplicationIcon(pkgName)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return (drawable as BitmapDrawable).bitmap
        else
            when (drawable) {
                is BitmapDrawable -> drawable.bitmap

                is AdaptiveIconDrawable -> {
                    val layerDrawable = LayerDrawable(arrayOf(drawable.background, drawable.foreground))
                    val width = layerDrawable.intrinsicWidth
                    val height = layerDrawable.intrinsicHeight
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
                    layerDrawable.draw(canvas)
                    bitmap
                }
                else -> null
            }
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
        null
    }
}