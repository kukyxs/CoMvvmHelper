package com.kk.android.comvvmhelper.abs

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.kk.android.comvvmhelper.helper.SingletonHelperArg0

/**
 * @author kuky.
 * @description
 */
abstract class AbsImageEngine {
    abstract fun loadImageDrawable(view: ImageView, drawable: Drawable?, placeholder: Drawable?, errorHolder: Drawable?)

    abstract fun loadCircleImageDrawable(view: ImageView, drawable: Drawable?, placeholder: Drawable?, errorHolder: Drawable?, radius: Int?)

    abstract fun loadImagePath(view: ImageView, urlOrPath: String?, placeholder: Drawable?, errorHolder: Drawable?)

    abstract fun loadCircleImagePath(view: ImageView, urlOrPath: String?, placeholder: Drawable?, errorHolder: Drawable?, radius: Int?)

    abstract fun loadBackgroundDrawable(view: View, backgroundRes: Drawable)

    abstract fun loadBackgroundPath(view: View, backgroundUrlOrPath: String)
}

class ImageLoadHelper {

    companion object : SingletonHelperArg0<ImageLoadHelper>(::ImageLoadHelper)

    var engine: AbsImageEngine? = null
}