package com.kk.android.comvvmhelper.listener

import android.view.View

/**
 * @author kuky.
 * @description
 */
fun interface OnItemClickListener {
    fun onItemClick(position: Int, view: View?)
}