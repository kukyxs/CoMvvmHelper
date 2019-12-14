@file:Suppress("MemberVisibilityCanBePrivate")

package com.kuky.android.comvvmhelper.ui

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.kuky.android.comvvmhelper.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description
 */
abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment(), CoroutineScope by MainScope() {

    protected lateinit var mBinding: VB
    private var mSavedState = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setStyleAndAnimations()
        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mSavedState = true
        super.onSaveInstanceState(outState)
    }

    /**
     * we suggest use this method to show dialog fragment instead of [show]
     */
    fun showAllowStateLoss(manager: FragmentManager, tag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.isStateSaved) return
        }

        if (mSavedState) return

        show(manager, tag)
    }

    override fun onStart() {
        super.onStart()
        setDialogWindowAttributes()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
        cancel()
    }

    /**
     * override this method to change dialog style and animations in subclass
     */
    open fun setStyleAndAnimations() {
        setStyle(
            STYLE_NO_FRAME, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                android.R.style.Theme_Material_Dialog_Alert else android.R.style.Theme_Dialog
        )

        dialog?.window?.let {
            it.requestFeature(Window.FEATURE_NO_TITLE)
            it.setWindowAnimations(R.style.DialogPushInOutAnimation)
        }
    }

    /**
     * override this method to change this width/height/gravity/background of dialog
     */
    open fun setDialogWindowAttributes() {
        val attrs = dialog?.window?.attributes?.apply {
            width = (resources.displayMetrics.widthPixels * 0.8f).toInt()
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(0))
            attributes = attrs
        }
    }

    abstract fun layoutId(): Int

    abstract fun initDialog(view: View, savedInstanceState: Bundle?)
}