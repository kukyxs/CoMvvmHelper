package com.kk.android.comvvmhelper.listener

import android.view.View

/**
 * @author kuky.
 * @description
 */
fun interface OnRecyclerItemLongClickListener {
    fun onRecyclerItemLongClick(position: Int, view: View): Boolean
}