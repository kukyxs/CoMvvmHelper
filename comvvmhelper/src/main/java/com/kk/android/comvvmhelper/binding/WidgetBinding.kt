@file:Suppress("CHANGING_ARGUMENTS_EXECUTION_ORDER_FOR_NAMED_VARARGS")

package com.kk.android.comvvmhelper.binding

import android.graphics.drawable.Drawable
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
@BindingAdapter(
    value = ["bind:requestStatusCode", "bind:errorReloadListener",
        "bind:loadingHint", "bind:loadingDrawable", "bind:loadingColor",
        "bind:emptyHint", "bind:emptyDrawable", "bind:emptyColor",
        "bind:errorHint", "bind:errorDrawable", "bind:errorColor"], requireAll = false
)
fun bindRequestStatus(
    statusView: RequestStatusView,
    requestStatusCode: RequestStatusCode?, errorReload: OnErrorReloadListener?,
    loadingHint: String?, loadingTag: Drawable?, loadingColor: Int?,
    emptyHint: String?, emptyTag: Drawable?, emptyColor: Int?,
    errorHint: String?, errorTag: Drawable?, errorColor: Int?
) {
    statusView.injectRequestStatus(requestStatusCode ?: RequestStatusCode.Succeed)
    statusView.errorReload = errorReload
    statusView.loadingHint(loadingHint, loadingTag, loadingColor)
    statusView.emptyHint(emptyHint, emptyTag, emptyColor)
    statusView.errorHint(errorHint, errorTag, errorColor)
}