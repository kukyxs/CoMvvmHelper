package com.kk.android.comvvmhelper.binding

import androidx.databinding.BindingAdapter
import com.kk.android.comvvmhelper.listener.OnErrorReloadListener
import com.kk.android.comvvmhelper.widget.RequestStatusCode
import com.kk.android.comvvmhelper.widget.RequestStatusView

/**
 * @author kuky.
 * @description
 */
@BindingAdapter(value = ["bind:requestStatusCode", "bind:errorReloadListener"], requireAll = false)
fun bindRequestStatus(statusView: RequestStatusView, requestStatusCode: RequestStatusCode?, errorReload: OnErrorReloadListener?) {
    statusView.injectRequestStatus(requestStatusCode ?: RequestStatusCode.Succeed)
    statusView.errorReload = errorReload
}