package com.kk.android.comvvmhelper.extension

/**
 * @author kuky.
 * @description
 */

/**
 * example:
 * data class Person(val name: String, val age: Int)
 *
 * if we want get the name list from person list
 *
 * val nameList = personList.transformToList{ it.name }
 */
fun <T, R> List<T>.transformToList(transform: (T) -> R): MutableList<R> =
    mutableListOf<R>().apply {
        this@transformToList.forEach { add(transform(it)) }
    }

/**
 * Deprecated and use [List.toSet] instead
 */
@Deprecated("use List.toSet", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("List.toSet"))
fun <T, R> List<T>.transformToSet(transform: (T) -> R): Set<R> =
    hashSetOf<R>().apply {
        this@transformToSet.forEach { add(transform(it)) }
    }

/**
 * transform List<String> to string, connect by connKey
 * Deprecated and use [List.joinToString] instead
 */
@Deprecated("use List.joinToString", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("List.joinToString"))
fun List<String>.transformToString(connKey: String = ","): String {
    if (size == 0) return ""

    val resultSb = StringBuffer()

    for (s in this) {
        resultSb.append(s).append(connKey)
    }

    return resultSb.contains(connKey)
        .yes { resultSb.substring(0, resultSb.length - 1) }
        .otherwise { resultSb.toString() }
}