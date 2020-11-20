@file:Suppress("CHANGING_ARGUMENTS_EXECUTION_ORDER_FOR_NAMED_VARARGS")

package com.kk.android.comvvmhelper.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kk.android.comvvmhelper.abs.ImageLoadHelper
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes

/**
 * @author kuky.
 * @description BindingAdapter for Glide loading picture
 */

/**
 * @param urlOrPath picture url
 * @param isCircle
 * @param radius radius for circle image, if isCircle is false it not worked
 */
@BindingAdapter(value = ["bind:imgUrl", "bind:placeHolder", "bind:error", "bind:isCircle", "bind:radius"], requireAll = false)
fun bindImage(view: ImageView, urlOrPath: String?, placeholder: Drawable?, error: Drawable?, isCircle: Boolean? = false, radius: Int?) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }

    engine.let {
        (isCircle ?: false).yes {
            it.loadCircleImagePath(view, urlOrPath, placeholder, error, radius)
        }.otherwise {
            it.loadImagePath(view, urlOrPath, placeholder, error)
        }
    }
}

/**
 * @param imgRes picture resource
 * @param isCircle
 * @param radius radius for circle image, if isCircle is false it not worked
 */
@BindingAdapter(value = ["bind:imgRes", "bind:placeHolder", "bind:error", "bind:isCircle", "bind:radius"], requireAll = false)
fun bindImage(view: ImageView, imgRes: Drawable?, placeholder: Drawable?, error: Drawable?, isCircle: Boolean? = false, radius: Int?) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }

    engine.let {
        (isCircle ?: false).yes {
            it.loadCircleImageDrawable(view, imgRes, placeholder, error, radius)
        }.otherwise {
            it.loadImageDrawable(view, imgRes, placeholder, error)
        }
    }
}

/**
 * @param backgroundUrl
 */
@BindingAdapter("bind:backgroundPath")
fun bindBackground(view: View, backgroundUrl: String) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }
    engine.loadBackgroundPath(view, backgroundUrl)
}

/**
 * @param backgroundRes
 */
@BindingAdapter("bind:backgroundRes")
fun bindBackground(view: View, backgroundRes: Drawable) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }
    engine.loadBackgroundDrawable(view, backgroundRes)
}