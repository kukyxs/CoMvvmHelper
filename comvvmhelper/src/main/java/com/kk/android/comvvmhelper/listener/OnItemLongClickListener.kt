package com.kk.android.comvvmhelper.listener

import android.view.View

/**
 * @author kuky.
 * @description
 */
fun interface OnItemLongClickListener {
    fun onItemLongClick(position: Int, view: View?): Boolean
}