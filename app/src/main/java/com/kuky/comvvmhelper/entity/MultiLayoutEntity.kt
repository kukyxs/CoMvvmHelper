package com.kuky.comvvmhelper.entity

/**
 * @author kuky.
 * @description
 */

sealed class MultiLayoutEntity(val type: Int) {

    object IntLayout : MultiLayoutEntity(0)

    object StringLayout : MultiLayoutEntity(1)
}