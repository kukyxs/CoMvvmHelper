package com.kk.android.comvvmhelper.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes

/**
 * @author kuky.
 * @description
 */
@BindingAdapter(value = ["bind:imgUrl", "bind:placeHolder", "bind:error", "bind:isCircle", "bind:radius"], requireAll = false)
fun bindImage(view: ImageView, urlOrPath: String, placeholder: Drawable?, error: Drawable?, isCircle: Boolean? = false, radius: Int?) {
    val request = (isCircle ?: false).yes {
        RequestOptions.bitmapTransform(RoundedCorners(radius ?: 360))
    }.otherwise { RequestOptions.centerCropTransform() }

    placeholder?.let { request.placeholder(it) }

    error?.let { request.error(it) }

    Glide.with(view).load(urlOrPath).apply(request).into(view)
}

@BindingAdapter(value = ["bind:imgRes", "bind:placeHolder", "bind:error", "bind:isCircle", "bind:radius"], requireAll = false)
fun bindImage(view: ImageView, imgRes: Drawable, placeholder: Drawable?, error: Drawable?, isCircle: Boolean? = false, radius: Int?) {
    val request = (isCircle ?: false).yes {
        RequestOptions.bitmapTransform(RoundedCorners(radius ?: 360))
    }.otherwise { RequestOptions.centerCropTransform() }

    placeholder?.let { request.placeholder(it) }

    error?.let { request.error(it) }

    Glide.with(view.context).load(imgRes).apply(request).into(view)
}