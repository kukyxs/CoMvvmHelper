package com.kk.android.comvvmhelper.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * @author kuky.
 * @description
 */
@BindingAdapter("bind:circleImg")
fun loadCircleImage(view: ImageView, urlOrPath: String) {
    Glide.with(view.context)
        .load(urlOrPath).apply(RequestOptions.bitmapTransform(RoundedCorners(360)))
        .into(view)
}

@BindingAdapter("bind:circleImgRes")
fun loadCircleImage(view: ImageView, image: Drawable) {
    Glide.with(view.context)
        .load(image).apply(RequestOptions.bitmapTransform(RoundedCorners(360)))
        .into(view)
}

@BindingAdapter(value = ["bind:imgUrl", "bind:placeHolder", "bind:errorHolder"], requireAll = false)
fun loadImage(view: ImageView, urlOrPath: String, placeholder: Drawable?, errorHolder: Drawable?) {
    val request = RequestOptions.centerCropTransform()

    if (placeholder != null) request.placeholder(placeholder)

    if (errorHolder != null) request.error(errorHolder)

    Glide.with(view.context).load(urlOrPath).apply(request).into(view)
}

@BindingAdapter(value = ["bind:imgRes", "bind:place", "bind:error"], requireAll = false)
fun loadImage(view: ImageView, imgRes: Drawable, place: Drawable?, error: Drawable?) {
    val request = RequestOptions.centerCropTransform()

    if (place != null) request.placeholder(place)

    if (error != null) request.error(error)

    Glide.with(view.context).load(imgRes).apply(request).into(view)
}