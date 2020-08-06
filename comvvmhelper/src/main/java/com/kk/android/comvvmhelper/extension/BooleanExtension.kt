package com.kk.android.comvvmhelper.extension

/**
 * @author kuky.
 * @description
 */

sealed class BooleanExtension<out T>

object Otherwise : BooleanExtension<Nothing>()

class DataTransformer<T>(val data: T) : BooleanExtension<T>()

inline fun <T> Boolean.yes(block: () -> T): BooleanExtension<T> =
    when {
        this -> DataTransformer(block())
        else -> Otherwise
    }

inline fun <T> BooleanExtension<T>.otherwise(block: () -> T): T =
    when (this) {
        is Otherwise -> block()
        is DataTransformer<T> -> data
    }