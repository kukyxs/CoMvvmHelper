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

/**
 * @author kuky.
 * @description example for AbsImageEngine
 * if use ImageViewBinding ViewDataBindingAdapter, inject this into startCov at Application
 * @see com.kuky.comvvmhelper.App
 */
class GlideEngine : AbsImageEngine() {

    override fun loadImageDrawable(view: ImageView, drawable: Drawable?, placeholder: Drawable?, errorHolder: Drawable?) {
        val request = RequestOptions.centerCropTransform()
        placeholder?.let { request.placeholder(it) }
        errorHolder?.let { request.error(it) }
        Glide.with(view).load(drawable).apply(request).into(view)
    }

    override fun loadCircleImageDrawable(view: ImageView, drawable: Drawable?, placeholder: Drawable?, errorHolder: Drawable?, radius: Int?) {
        val request = RequestOptions.bitmapTransform(RoundedCorners(radius ?: 360))
        placeholder?.let { request.placeholder(it) }
        errorHolder?.let { request.error(it) }
        Glide.with(view).load(drawable).apply(request).into(view)
    }

    override fun loadImagePath(view: ImageView, urlOrPath: String?, placeholder: Drawable?, errorHolder: Drawable?) {
        val request = RequestOptions.centerCropTransform()
        placeholder?.let { request.placeholder(it) }
        errorHolder?.let { request.error(it) }
        Glide.with(view).load(urlOrPath).apply(request).into(view)
    }

    override fun loadCircleImagePath(view: ImageView, urlOrPath: String?, placeholder: Drawable?, errorHolder: Drawable?, radius: Int?) {
        val request = RequestOptions.bitmapTransform(RoundedCorners(radius ?: 360))
        placeholder?.let { request.placeholder(it) }
        errorHolder?.let { request.error(it) }
        Glide.with(view).load(urlOrPath).apply(request).into(view)
    }

    override fun loadBackgroundDrawable(view: View, backgroundRes: Drawable) {
        val customTarget = object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                view.background = placeholder
            }
        }

        Glide.with(view).load(backgroundRes).into(customTarget)
    }

    override fun loadBackgroundPath(view: View, backgroundUrlOrPath: String) {
        val customTarget = object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                view.background = placeholder
            }
        }

        Glide.with(view).load(backgroundUrlOrPath).into(customTarget)
    }
}