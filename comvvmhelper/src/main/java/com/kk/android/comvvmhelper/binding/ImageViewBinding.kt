package com.kk.android.comvvmhelper.binding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
    val request = (isCircle ?: false).yes {
        RequestOptions.bitmapTransform(RoundedCorners(radius ?: 360))
    }.otherwise { RequestOptions.centerCropTransform() }

    placeholder?.let { request.placeholder(it) }

    error?.let { request.error(it) }

    Glide.with(view).load(urlOrPath ?: "").apply(request).into(view)
}

/**
 * @param imgRes picture resource
 * @param isCircle
 * @param radius radius for circle image, if isCircle is false it not worked
 */
@BindingAdapter(value = ["bind:imgRes", "bind:placeHolder", "bind:error", "bind:isCircle", "bind:radius"], requireAll = false)
fun bindImage(view: ImageView, imgRes: Drawable?, placeholder: Drawable?, error: Drawable?, isCircle: Boolean? = false, radius: Int?) {
    val request = (isCircle ?: false).yes {
        RequestOptions.bitmapTransform(RoundedCorners(radius ?: 360))
    }.otherwise { RequestOptions.centerCropTransform() }

    placeholder?.let { request.placeholder(it) }

    error?.let { request.error(it) }

    Glide.with(view.context).load(imgRes ?: ColorDrawable(Color.TRANSPARENT)).apply(request).into(view)
}

/**
 * @param backgroundUrl
 */
@BindingAdapter("bind:backgroundPath")
fun bindBackground(view: View, backgroundUrl: String) {
    val customTarget = object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            view.background = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            view.background = placeholder
        }
    }

    Glide.with(view.context).load(backgroundUrl).into(customTarget)
}

/**
 * @param backgroundRes
 */
@BindingAdapter("bind:backgroundRes")
fun bindBackground(view: View, backgroundRes: Drawable) {
    val customTarget = object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            view.background = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            view.background = placeholder
        }
    }

    Glide.with(view.context).load(backgroundRes).into(customTarget)
}