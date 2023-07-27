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
    abstract fun loadImageData(view: ImageView, imageData: Any?, placeholderId: Drawable?, errorHolderId: Drawable?, radius: Int?)

    abstract fun loadBackgroundData(view: View, backgroundData: Any)
}

class ImageLoadHelper {

    companion object : SingletonHelperArg0<ImageLoadHelper>(::ImageLoadHelper)

    var engine: AbsImageEngine? = null

    val loader: AbsImageEngine get() = engine!!
}