package com.kuky.comvvmhelper.helper

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.kk.android.comvvmhelper.abs.AbsImageEngine

/**
 * @author kuky.
 * @description example for AbsImageEngine
 */
class CoilEngine : AbsImageEngine() {
    override fun loadImageDrawable(view: ImageView, drawable: Drawable?, placeholder: Drawable?, errorHolder: Drawable?) {
        view.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.load(drawable) {
            crossfade(true)
            placeholder?.let { placeholder(it) }
            errorHolder?.let { error(it) }
        }
    }

    override fun loadCircleImageDrawable(view: ImageView, drawable: Drawable?, placeholder: Drawable?, errorHolder: Drawable?, radius: Int?) {
        view.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.load(drawable) {
            crossfade(true)
            transformations(RoundedCornersTransformation(radius?.toFloat() ?: 360f))
            placeholder?.let { placeholder(it) }
            errorHolder?.let { error(it) }
        }
    }

    override fun loadImagePath(view: ImageView, urlOrPath: String?, placeholder: Drawable?, errorHolder: Drawable?) {
        view.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.load(urlOrPath) {
            crossfade(true)
            placeholder?.let { placeholder(it) }
            errorHolder?.let { error(it) }
        }
    }

    override fun loadCircleImagePath(view: ImageView, urlOrPath: String?, placeholder: Drawable?, errorHolder: Drawable?, radius: Int?) {
        view.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.load(urlOrPath) {
            crossfade(true)
            transformations(RoundedCornersTransformation(radius?.toFloat() ?: 360f))
            placeholder?.let { placeholder(it) }
            errorHolder?.let { error(it) }
        }
    }

    override fun loadBackgroundDrawable(view: View, backgroundRes: Drawable) {
        val request = ImageRequest.Builder(view.context)
            .data(backgroundRes).target(
                onStart = { placeholder ->
                    view.background = placeholder
                }, onError = { error ->
                    view.background = error
                }, onSuccess = { result ->
                    view.background = result
                }
            ).build()
        ImageLoader.Builder(view.context).build().enqueue(request)
    }

    override fun loadBackgroundPath(view: View, backgroundUrlOrPath: String) {
        val request = ImageRequest.Builder(view.context)
            .data(backgroundUrlOrPath).target(
                onStart = { placeholder ->
                    view.background = placeholder
                }, onError = { error ->
                    view.background = error
                }, onSuccess = { result ->
                    view.background = result
                }
            ).build()
        ImageLoader.Builder(view.context).build().enqueue(request)
    }
}