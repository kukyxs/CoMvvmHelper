package com.kuky.comvvmhelper.helper

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.kk.android.comvvmhelper.abs.AbsImageEngine
import com.kk.android.comvvmhelper.utils.dp2px

/**
 * @author kuky.
 * @description example for AbsImageEngine
 */
class CoilEngine : AbsImageEngine() {
    override fun loadImageData(view: ImageView, imageData: Any?, placeholderId: Drawable?, errorHolderId: Drawable?, radius: Int?) {
        view.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.load(imageData) {
            crossfade(true)
            placeholderId?.let { placeholder(it) }
            errorHolderId?.let { error(it) }
            if (radius != null) {
                transformations(RoundedCornersTransformation(radius.toFloat().dp2px()))
            }
        }
    }

    override fun loadBackgroundData(view: View, backgroundData: Any) {
        val request = ImageRequest.Builder(view.context)
            .data(backgroundData).target(
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