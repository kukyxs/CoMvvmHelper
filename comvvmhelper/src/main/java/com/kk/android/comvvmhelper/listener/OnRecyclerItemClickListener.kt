package com.kk.android.comvvmhelper.listener

import android.view.View

/**
 * @author kuky.
 * @description
 */
fun interface OnRecyclerItemClickListener {
    fun onRecyclerItemClick(position: Int, view: View)
}