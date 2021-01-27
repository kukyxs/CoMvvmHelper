@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import androidx.databinding.ViewDataBinding
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.anno.StatusBarTextColorMode
import com.kk.android.comvvmhelper.anno.WindowState
import com.kk.android.comvvmhelper.extension.layoutToDataBinding
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import com.kk.android.comvvmhelper.helper.ActivityStackManager
import com.kk.android.comvvmhelper.helper.KLogger
import com.kk.android.comvvmhelper.helper.iPrint
import com.kk.android.comvvmhelper.utils.setColorForStatusBar
import com.kk.android.comvvmhelper.utils.setStatusBarDarkMode
import com.kk.android.comvvmhelper.utils.setStatusBarLightMode
import com.kk.android.comvvmhelper.utils.translucentStatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), CoroutineScope by MainScope(), KLogger {
    protected val mBinding: VB by lazy {
        layoutId().layoutToDataBinding(this)
    }

    private val mActivityConfig by lazy {
        javaClass.getAnnotation(ActivityConfig::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStackManager.addActivity(this)
        mBinding.lifecycleOwner = this
        setStatusBarAnnotationState()
        initActivity(savedInstanceState)
    }

    @Suppress("DEPRECATION", "CascadeIf")
    private fun setStatusBarAnnotationState() {
        mActivityConfig?.let { config ->
            if (config.windowState == WindowState.TRANSPARENT_STATUS_BAR) {
                // make sure the decorView has been created
                window.decorView.postDelayed(10) {
                    translucentStatusBar(config.contentUpToStatusBarWhenTransparent)
                }
            } else {
                val colorRegex = Regex("#([0-9A-Fa-f]{8}|[0-9A-Fa-f]{6})")
                val statusBarColor = config.statusBarColorString.matches(colorRegex).yes {
                    Color.parseColor(config.statusBarColorString)
                }.otherwise { config.statusBarColor }
                setColorForStatusBar(statusBarColor)
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                iPrint { "change text color style for status bar only worked on or above Android M(23)" }
            } else {
                (config.statusBarTextColorMode == StatusBarTextColorMode.DARK)
                    .yes { setStatusBarLightMode() }.otherwise { setStatusBarDarkMode() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        mBinding.unbind()
        ActivityStackManager.removeActivity(this)
    }

    abstract fun layoutId(): Int

    abstract fun initActivity(savedInstanceState: Bundle?)
}