package com.kuky.comvvmhelper.helper

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kk.android.comvvmhelper.abs.AbsImageEngine
import com.kk.android.comvvmhelper.utils.dp2px

/**
 * @author kuky.
 * @description example for AbsImageEngine
 * if use ImageViewBinding ViewDataBindingAdapter, inject this into startCov at Application
 * @see com.kuky.comvvmhelper.App
 */
class GlideEngine : AbsImageEngine() {
    override fun loadImageData(view: ImageView, imageData: Any?, placeholderId: Drawable?, errorHolderId: Drawable?, radius: Int?) {
        val request = if (radius != null) RequestOptions.bitmapTransform(RoundedCorners(radius.toFloat().dp2px().toInt()))
        else RequestOptions.centerCropTransform()

        placeholderId?.let { request.placeholder(it) }
        errorHolderId?.let { request.error(it) }
        Glide.with(view).load(imageData).apply(request).into(view)
    }

    override fun loadBackgroundData(view: View, backgroundData: Any) {
        val customTarget = object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                view.background = placeholder
            }
        }

        Glide.with(view).load(backgroundData).into(customTarget)
    }
}