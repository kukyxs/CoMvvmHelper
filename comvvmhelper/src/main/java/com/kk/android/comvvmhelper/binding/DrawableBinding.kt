@file:Suppress("DEPRECATION")

package com.kk.android.comvvmhelper.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import androidx.annotation.IntDef
import androidx.databinding.BindingAdapter
import com.kk.android.comvvmhelper.utils.dp2px

/**
 * @author kuky.
 * @description create gradient drawable by dataBinding,
 * not support Ring type because of no open api for this,
 * do not use for recycler view item
 */

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
@BindingAdapter(
    value = [
        "drawable_def", "drawable_checked", "drawable_checkable",
        "drawable_enabled", "drawable_focus", "drawable_pressed",
        "drawable_selected"
    ], requireAll = false
)
fun setViewStateDrawables(
    view: View, drawableDef: Drawable?, drawableChecked: Drawable?,
    drawableCheckable: Drawable?, drawableEnabled: Drawable?, drawableFocus: Drawable?,
    drawablePressed: Drawable?, drawableSelected: Drawable?
) {
    val def = drawableDef ?: view.background

    if (drawableChecked == null && drawableCheckable == null && drawableEnabled == null &&
        drawableFocus == null && drawablePressed == null && drawableSelected == null
    ) {
        view.background = def
        return
    }

    StateListDrawable().run {
        drawableChecked.appendToState(this, intArrayOf(android.R.attr.state_checked))
        drawableCheckable.appendToState(this, intArrayOf(android.R.attr.state_checkable))
        drawableEnabled.appendToState(this, intArrayOf(android.R.attr.state_enabled))
        drawableFocus.appendToState(this, intArrayOf(android.R.attr.state_focused))
        drawablePressed.appendToState(this, intArrayOf(android.R.attr.state_pressed))
        drawableSelected.appendToState(this, intArrayOf(android.R.attr.state_selected))
        def.appendToState(this, intArrayOf(0))
        view.background = this
    }
}

internal fun Drawable?.appendToState(proxyListDrawable: StateListDrawable, stateSet: IntArray?) {
    if (this != null) proxyListDrawable.addState(stateSet, this)
}

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
@BindingAdapter(
    value = [
        "shape_mode", "solid_color", "stroke_color",
        "stroke_width", "stroke_dash", "stroke_dash_gap",
        "radius", "radius_lt", "radius_lb", "radius_rt", "radius_rb",
        "start_color", "center_color", "end_color", "gradient_orientation", "gradient_type",
        "radial_center_x", "radial_center_y", "radial_radius"
    ], requireAll = false
)
fun setViewBackground(
    view: View, @ShapeMode shapeMode: Int?, solidColor: String?, strokeColor: String?,
    strokeWidth: Float?, strokeDash: Float?, strokeDashGap: Float?,
    radius: Float?, radiusLT: Float?, radiusLB: Float?, radiusRT: Float?, radiusRB: Float?,
    startColor: String?, centerColor: String?, endColor: String?, @GradientOrientation gradientOrientation: Int?, @GradientType gradientType: Int?,
    radialCenterX: Float?, radialCenterY: Float?, radialRadius: Float?
) {
    view.background = createGradientDrawable(
        shapeMode, solidColor, strokeColor, strokeWidth, strokeDash, strokeDashGap,
        radius, radiusLT, radiusLB, radiusRT, radiusRB, startColor, centerColor, endColor,
        gradientOrientation, gradientType, radialCenterX, radialCenterY, radialRadius
    )
}

internal val colorRegex = Regex("#([0-9A-Fa-f]{8}|[0-9A-Fa-f]{6})")

internal fun String?.validateColor() = !isNullOrEmpty() && matches(colorRegex)

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
internal fun createGradientDrawable(
    @ShapeMode shapeMode: Int?, solidColor: String?, strokeColor: String?,
    strokeWidth: Float?, strokeDash: Float?, strokeDashGap: Float?,
    radius: Float?, radiusLT: Float?, radiusLB: Float?, radiusRT: Float?, radiusRB: Float?,
    startColor: String?, centerColor: String?, endColor: String?,
    @GradientOrientation gradientOrientation: Int?, @GradientType gradientType: Int?,
    radialCenterX: Float?, radialCenterY: Float?, radialRadius: Float?
): Drawable = GradientDrawable().apply {
    if (startColor.validateColor() && endColor.validateColor()) {
        val colors = if (centerColor.validateColor()) {
            intArrayOf(Color.parseColor(startColor), Color.parseColor(centerColor), Color.parseColor(endColor))
        } else {
            intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
        }
        setColors(colors)
        orientation = gradientOrientation.mapOrientation()
        setGradientType(gradientType ?: GradientType.LINEAR)
        if (gradientType == GradientType.RADIAL) {
            setGradientCenter(radialCenterX ?: 0.5f, radialCenterY ?: 0.5f)
            gradientRadius = radialRadius?.dp2px() ?: 0f
        }
    } else if (solidColor.validateColor()) {
        setColor(Color.parseColor(solidColor))
    } else {
        setColor(Color.TRANSPARENT)
    }

    shape = (shapeMode ?: ShapeMode.RECTANGLE).validateShapeMode()

    if (strokeWidth != null && strokeWidth > 0) {
        setStroke(strokeWidth.dp2px().toInt(), Color.parseColor(strokeColor ?: "#00000000"), (strokeDash ?: 0f).dp2px(), (strokeDashGap ?: 0f).dp2px())
    }

    if (radius == null || radius <= 0) {
        cornerRadii = floatArrayOf(
            (radiusLT ?: 0f).dp2px(), (radiusLT ?: 0f).dp2px(),
            (radiusRT ?: 0f).dp2px(), (radiusRT ?: 0f).dp2px(),
            (radiusRB ?: 0f).dp2px(), (radiusRB ?: 0f).dp2px(),
            (radiusLB ?: 0f).dp2px(), (radiusLB ?: 0f).dp2px()
        )
    } else {
        cornerRadius = radius.dp2px()
    }
}

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
@IntDef(value = [ShapeMode.RECTANGLE, ShapeMode.OVAL, ShapeMode.LINE])
@Retention(AnnotationRetention.SOURCE)
annotation class ShapeMode {
    companion object {
        const val RECTANGLE = GradientDrawable.RECTANGLE
        const val OVAL = GradientDrawable.OVAL
        const val LINE = GradientDrawable.LINE
    }
}

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
internal fun Int?.validateShapeMode(): Int =
    if (this == null || this > ShapeMode.LINE || this < ShapeMode.RECTANGLE) GradientDrawable.RECTANGLE else this

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
@IntDef(value = [GradientType.LINEAR, GradientType.RADIAL, GradientType.SWEEP])
@Retention(AnnotationRetention.SOURCE)
annotation class GradientType {
    companion object {
        const val LINEAR = 0
        const val RADIAL = 1
        const val SWEEP = 2
    }
}

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
@IntDef(
    value = [
        GradientOrientation.TOP_BOTTOM, GradientOrientation.TR_BL, GradientOrientation.RIGHT_LEFT, GradientOrientation.BR_TL,
        GradientOrientation.BOTTOM_TOP, GradientOrientation.BL_TR, GradientOrientation.LEFT_RIGHT, GradientOrientation.TL_BR
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class GradientOrientation {
    companion object {
        const val TOP_BOTTOM = 0
        const val TR_BL = 1
        const val RIGHT_LEFT = 2
        const val BR_TL = 3
        const val BOTTOM_TOP = 4
        const val BL_TR = 5
        const val LEFT_RIGHT = 6
        const val TL_BR = 7
    }
}

@Deprecated("do not use for recyclerview", replaceWith = ReplaceWith("do not use for recyclerview"))
internal fun Int?.mapOrientation(): GradientDrawable.Orientation =
    when (this) {
        GradientOrientation.TOP_BOTTOM -> GradientDrawable.Orientation.TOP_BOTTOM
        GradientOrientation.TR_BL -> GradientDrawable.Orientation.TR_BL
        GradientOrientation.RIGHT_LEFT -> GradientDrawable.Orientation.RIGHT_LEFT
        GradientOrientation.BR_TL -> GradientDrawable.Orientation.BR_TL
        GradientOrientation.BOTTOM_TOP -> GradientDrawable.Orientation.BOTTOM_TOP
        GradientOrientation.BL_TR -> GradientDrawable.Orientation.BL_TR
        GradientOrientation.LEFT_RIGHT -> GradientDrawable.Orientation.LEFT_RIGHT
        GradientOrientation.TL_BR -> GradientDrawable.Orientation.TL_BR
        else -> GradientDrawable.Orientation.TOP_BOTTOM
    }