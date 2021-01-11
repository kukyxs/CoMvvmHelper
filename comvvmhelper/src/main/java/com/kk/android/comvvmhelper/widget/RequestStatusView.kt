package com.kk.android.comvvmhelper.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import com.kk.android.comvvmhelper.R
import com.kk.android.comvvmhelper.extension.colorValue
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

    private var mStatusTextColors: MutableList<Int> = mutableListOf(
        context.colorValue(R.color.status_def_color),
        context.colorValue(R.color.status_def_color),
        context.colorValue(R.color.status_def_color)
    )

    private var mStatusTexts: MutableList<String> = mutableListOf(
        context.stringValue(R.string.data_loading_tag),
        context.stringValue(R.string.data_empty_tag),
        context.stringValue(R.string.data_reload_tag)
    )

    private var mStatusDrawables: MutableList<Drawable> = mutableListOf(
        context.drawableValue(R.drawable.tag_loading),
        context.drawableValue(R.drawable.tag_empty),
        context.drawableValue(R.drawable.tag_load_error)
    )

    var errorReload: OnErrorReloadListener? = null

    init {
        injectRequestStatus(RequestStatusCode.Succeed)
    }

    fun stateHint(
        textHints: MutableList<String>? = null,
        drawableHint: MutableList<Drawable>? = null,
        textColors: MutableList<Int>? = null
    ) {
        if (!textHints.isNullOrEmpty()) mStatusTexts = textHints

        if (!drawableHint.isNullOrEmpty()) mStatusDrawables = drawableHint

        if (!textColors.isNullOrEmpty()) mStatusTextColors = textColors
    }

    fun loadingHint(loadingHint: String? = null, loadingDrawable: Drawable? = null, color: Int? = null) {
        if (!loadingHint.isNullOrEmpty()) mStatusTexts[0] = loadingHint

        if (loadingDrawable != null) mStatusDrawables[0] = loadingDrawable

        if (color != null) mStatusTextColors[0] = color
    }

    fun loadingHint(@StringRes loadingHint: Int? = null, @DrawableRes loadingDrawable: Int? = null, @ColorRes colorRes: Int? = null) {
        if (loadingHint != null) mStatusTexts[0] = context.stringValue(loadingHint)

        if (loadingDrawable != null) mStatusDrawables[0] = context.drawableValue(loadingDrawable)

        if (colorRes != null) mStatusTextColors[0] = context.colorValue(colorRes)
    }

    fun emptyHint(emptyHint: String? = null, emptyDrawable: Drawable? = null, color: Int? = null) {
        if (!emptyHint.isNullOrEmpty()) mStatusTexts[1] = emptyHint

        if (emptyDrawable != null) mStatusDrawables[1] = emptyDrawable

        if (color != null) mStatusTextColors[1] = color
    }

    fun emptyHint(@StringRes emptyHint: Int? = null, @DrawableRes emptyDrawable: Int? = null, @ColorRes colorRes: Int? = null) {
        if (emptyHint != null) mStatusTexts[1] = context.stringValue(emptyHint)

        if (emptyDrawable != null) mStatusDrawables[1] = context.drawableValue(emptyDrawable)

        if (colorRes != null) mStatusTextColors[1] = context.colorValue(colorRes)
    }

    fun errorHint(errorHint: String? = null, errorDrawable: Drawable? = null, color: Int? = null) {
        if (!errorHint.isNullOrEmpty()) mStatusTexts[2] = errorHint

        if (errorDrawable != null) mStatusDrawables[2] = errorDrawable

        if (color != null) mStatusTextColors[2] = color
    }

    fun errorHint(@StringRes errorHint: Int? = null, @DrawableRes errorDrawable: Int? = null, @ColorRes colorRes: Int? = null) {
        if (errorHint != null) mStatusTexts[2] = context.stringValue(errorHint)

        if (errorDrawable != null) mStatusDrawables[2] = context.drawableValue(errorDrawable)

        if (colorRes != null) mStatusTextColors[2] = context.colorValue(colorRes)
    }

    fun injectRequestStatus(status: RequestStatusCode) {
        postDelayed(10) {
            val statusDrawable: Drawable?

            when (status) {
                RequestStatusCode.Loading -> {
                    statusDrawable = mStatusDrawables[0]; text = mStatusTexts[0]; setTextColor(mStatusTextColors[0])
                }

                RequestStatusCode.Empty -> {
                    statusDrawable = mStatusDrawables[1]; text = mStatusTexts[1]; setTextColor(mStatusTextColors[1])
                }

                RequestStatusCode.Error -> {
                    statusDrawable = mStatusDrawables[2]; text = mStatusTexts[2]; setTextColor(mStatusTextColors[2])
                    setOnClickListener { errorReload?.onDataReload() }
                }

                RequestStatusCode.Succeed -> {
                    isVisible = false; return@postDelayed
                }
            }

            isVisible = true
            statusDrawable.setBounds(0, 0, statusDrawable.minimumWidth / 2, statusDrawable.minimumHeight / 2)
            compoundDrawablePadding = 12
            setCompoundDrawables(null, statusDrawable, null, null)
        }
    }
}