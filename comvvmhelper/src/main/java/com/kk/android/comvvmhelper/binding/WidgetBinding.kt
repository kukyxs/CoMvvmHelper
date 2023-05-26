package com.kk.android.comvvmhelper.binding

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.kk.android.comvvmhelper.extension.setOnDebounceClickListener
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
    value = ["requestStatusCode", "errorReloadListener",
        "loadingHint", "loadingDrawable", "loadingColor",
        "emptyHint", "emptyDrawable", "emptyColor",
        "errorHint", "errorDrawable", "errorColor"], requireAll = false
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

@BindingAdapter("onDebounceClick")
fun bindViewDebounce(view: View, onClick: View.OnClickListener?) {
    view.setOnDebounceClickListener { v -> onClick?.onClick(v) }
}


@BindingAdapter(value = ["isVisible"])
fun viewVisible(v: View, isVisible: Boolean) {
    v.isVisible = isVisible
}

@BindingAdapter(value = ["isInvisible"])
fun viewInvisible(v: View, isInvisible: Boolean) {
    v.isInvisible = isInvisible
}