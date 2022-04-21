@file:Suppress("CHANGING_ARGUMENTS_EXECUTION_ORDER_FOR_NAMED_VARARGS")

package com.kk.android.comvvmhelper.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kk.android.comvvmhelper.abs.ImageLoadHelper

/**
 * @author kuky.
 * @description BindingAdapter for Glide loading picture
 */

/**
 * @param urlOrPath picture url
 * @param isCircle
 * @param radius radius for circle image, if isCircle is false it not worked
 */
@BindingAdapter(value = ["imgUrl", "placeHolder", "error", "isCircle", "radius"], requireAll = false)
fun bindImage(view: ImageView, urlOrPath: String?, placeholder: Drawable?, error: Drawable?, isCircle: Boolean? = false, radius: Int?) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }

    if (isCircle == true) {
        engine.loadCircleImagePath(view, urlOrPath, placeholder, error, radius)
    } else {
        engine.loadImagePath(view, urlOrPath, placeholder, error)
    }
}

/**
 * @param imgRes picture resource
 * @param isCircle
 * @param radius radius for circle image, if isCircle is false it not worked
 */
@BindingAdapter(value = ["imgRes", "placeHolder", "error", "isCircle", "radius"], requireAll = false)
fun bindImage(view: ImageView, imgRes: Drawable?, placeholder: Drawable?, error: Drawable?, isCircle: Boolean? = false, radius: Int?) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }

    if (isCircle == true) {
        engine.loadCircleImageDrawable(view, imgRes, placeholder, error, radius)
    } else {
        engine.loadImageDrawable(view, imgRes, placeholder, error)
    }
}

/**
 * @param backgroundUrl
 */
@BindingAdapter("backgroundPath")
fun bindBackground(view: View, backgroundUrl: String) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }
    engine.loadBackgroundPath(view, backgroundUrl)
}

/**
 * @param backgroundRes
 */
@BindingAdapter("backgroundRes")
fun bindBackground(view: View, backgroundRes: Drawable) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }
    engine.loadBackgroundDrawable(view, backgroundRes)
}