@file:Suppress("CHANGING_ARGUMENTS_EXECUTION_ORDER_FOR_NAMED_VARARGS")

package com.kk.android.comvvmhelper.binding

import androidx.databinding.BindingAdapter
import com.kk.android.comvvmhelper.listener.OnErrorReloadListener
import com.kk.android.comvvmhelper.widget.RequestStatusCode
import com.kk.android.comvvmhelper.widget.RequestStatusView

/**
 * @author kuky.
 * @description BindingAdapter for Custom View
 */

/**
 * @param requestStatusCode status code for data loading, default is [RequestStatusCode.Succeed]
 * @param errorReload click to reload data
 */
@BindingAdapter(value = ["bind:requestStatusCode", "bind:errorReloadListener"], requireAll = false)
fun bindRequestStatus(statusView: RequestStatusView, requestStatusCode: RequestStatusCode?, errorReload: OnErrorReloadListener?) {
    statusView.injectRequestStatus(requestStatusCode ?: RequestStatusCode.Succeed)
    statusView.errorReload = errorReload
}