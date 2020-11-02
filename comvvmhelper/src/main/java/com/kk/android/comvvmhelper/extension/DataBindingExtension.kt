package com.kk.android.comvvmhelper.extension

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author kuky.
 * @description
 */

fun <T : ViewDataBinding> Int.layoutToDataBinding(context: Context, parent: ViewGroup? = null, attached: Boolean = false) =
    DataBindingUtil.inflate<T>(LayoutInflater.from(context), this, parent, attached)