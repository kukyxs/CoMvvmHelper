@file:Suppress("DEPRECATION")

package com.kk.android.comvvmhelper.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @author kuky.
 * @description
 */
fun Context.getAppName(): String = try {
    val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
    val labelRes = packageInfo.applicationInfo.labelRes
    resources.getString(labelRes)
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    ""
}

fun Context.getAppVersionName(): String = try {
    val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
    packageInfo.versionName
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    ""
}

fun Context.getAppVersionCode(): Long =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) getLongAppVersion() else getAppIntVersion().toLong()

private fun Context.getAppIntVersion(): Int = try {
    packageManager.getPackageInfo(packageName, 0).versionCode
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    0
}

@RequiresApi(Build.VERSION_CODES.P)
private fun Context.getLongAppVersion(): Long = try {
    packageManager.getPackageInfo(packageName, 0).longVersionCode
} catch (e: NameNotFoundException) {
    e.printStackTrace()
    0L
}

fun Context.starApp(packageName: String, fail: () -> Unit) =
    try {
        startActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            component = packageManager.getLaunchIntentForPackage(packageName)?.component
        })
    } catch (e: Exception) {
        e.printStackTrace()
        fail()
    }

fun Context.apkIconByDrawable(apkPath: String): Drawable? {
    packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)?.let {
        val appInfo = it.applicationInfo
        appInfo.sourceDir = apkPath
        appInfo.publicSourceDir = apkPath
        return appInfo.loadIcon(packageManager)
    }

    return null
}

fun Context.appIconByDrawable(pkgName: String): Drawable? {
    try {
        packageManager.let {
            return it.getApplicationInfo(pkgName, 0).loadIcon(it)
        }
    } catch (e: NameNotFoundException) {
        return null
    }
}

fun Context.appIconByBitmap(pkgName: String): Bitmap? = packageManager.let { pm ->
    try {
        val drawable = pm.getApplicationIcon(pkgName)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return (drawable as BitmapDrawable).bitmap
        else
            when (drawable) {
                is BitmapDrawable -> drawable.bitmap

                is AdaptiveIconDrawable -> {
                    val layerDrawable =
                        LayerDrawable(arrayOf(drawable.background, drawable.foreground))
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