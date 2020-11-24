package com.kk.android.comvvmhelper.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.kk.android.comvvmhelper.R
import com.kk.android.comvvmhelper.extension.drawableValue
import com.kk.android.comvvmhelper.extension.stringValue
import com.kk.android.comvvmhelper.listener.OnErrorReloadListener

/**
 * @author kuky.
 * @description
 */
open class RequestStatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CenterDrawableTextView(context, attrs, defStyleAttr) {

    var errorReload: OnErrorReloadListener? = null

    init {
        injectRequestStatus(RequestStatusCode.Succeed)
    }

    fun injectRequestStatus(status: RequestStatusCode) {
        val statusDrawable: Drawable?

        when (status) {
            RequestStatusCode.Loading -> {
                statusDrawable = context.drawableValue(R.drawable.tag_loading)
                text = context.stringValue(R.string.data_loading_tag)
            }

            RequestStatusCode.Empty -> {
                statusDrawable = context.drawableValue(R.drawable.tag_empty)
                text = context.stringValue(R.string.data_empty_tag)
            }

            RequestStatusCode.Error -> {
                statusDrawable = context.drawableValue(R.drawable.tag_load_error)
                text = context.stringValue(R.string.data_reload_tag)
                setOnClickListener { errorReload?.onDataReload() }
            }

            RequestStatusCode.Succeed -> {
                isVisible = false
                return
            }
        }

        isVisible = true
        statusDrawable.setBounds(0, 0, statusDrawable.minimumWidth / 2, statusDrawable.minimumHeight / 2)
        compoundDrawablePadding = 12
        setCompoundDrawables(null, statusDrawable, null, null)
        setTextColor(Color.parseColor("#FFCCCCCC"))
    }
}