package com.kuky.comvvmhelper.entity

import com.kk.android.comvvmhelper.listener.MultiLayoutImp

/**
 * @author kuky.
 * @description
 */

data class IntLayoutEntity(val index: Int = 0) : MultiLayoutImp {
    override fun viewType() = 0xFF01
}

data class StringLayoutEntity(val text: String = "empty") : MultiLayoutImp {
    override fun viewType() = 0xFF02
}