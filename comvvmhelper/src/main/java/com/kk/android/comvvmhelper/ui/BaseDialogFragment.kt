@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.kk.android.comvvmhelper.R
import com.kk.android.comvvmhelper.entity.DialogDisplayConfig
import com.kk.android.comvvmhelper.helper.KLogger
import com.kk.android.comvvmhelper.listener.OnDialogFragmentCancelListener
import com.kk.android.comvvmhelper.listener.OnDialogFragmentDismissListener
import com.kk.android.comvvmhelper.utils.screenWidth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description
 */
abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment(), CoroutineScope by MainScope(), KLogger {
    var onDialogFragmentDismissListener: OnDialogFragmentDismissListener? = null
    var onDialogFragmentCancelListener: OnDialogFragmentCancelListener? = null

    protected lateinit var mBinding: VB
    private var mSavedState = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setStyle(
            STYLE_NO_FRAME,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                android.R.style.Theme_Material_Dialog_Alert else android.R.style.Theme_Dialog
        )

        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setWindowAnimations(dialogFragmentAnim())
        }

        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mSavedState = true
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val isShow = showsDialog
        showsDialog = false
        super.onActivityCreated(savedInstanceState)
        showsDialog = isShow

        val view = view
        if (view != null) {
            if (view.parent != null) {
                throw IllegalStateException("DialogFragment can not be attached to a container view")
            }
            dialog?.setContentView(view)
        }

        activity?.let { dialog?.setOwnerActivity(it) }
        savedInstanceState?.let {
            val dialogState = it.getBundle("android:savedDialogState")
            if (dialogState != null) dialog?.onRestoreInstanceState(dialogState)
        }
    }

    /**
     * we suggest use this method to show dialog fragment instead of [show]
     */
    open fun showAllowStateLoss(manager: FragmentManager, tag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.isStateSaved) return
        }

        if (mSavedState) return

        show(manager, tag)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val dialogDisplayConfig = dialogFragmentDisplayConfigs()
            setBackgroundDrawable(dialogDisplayConfig.dialogBackground)
            attributes = dialog?.window?.attributes?.apply {
                width = dialogDisplayConfig.dialogWidth
                height = dialogDisplayConfig.dialogHeight
                gravity = dialogDisplayConfig.dialogGravity
            }
        }
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogFragmentDismissListener?.onDialogFragmentDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDialogFragmentCancelListener?.onDialogFragmentCancel(dialog)
    }

    // self define dialog fragment enter and exit animation
    open fun dialogFragmentAnim() = R.style.DialogPushInOutAnimation

    open fun dialogFragmentDisplayConfigs() = DialogDisplayConfig((screenWidth * 0.75).toInt())

    abstract fun layoutId(): Int

    abstract fun initDialog(view: View, savedInstanceState: Bundle?)

    // self define dialog fragment attributes
    @Deprecated("use dialogFragmentDisplayConfigs replaced", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("dialogFragmentDisplayConfigs()"))
    open fun dialogFragmentAttributes() = dialog?.window?.attributes?.apply {
        width = (requireActivity().resources.displayMetrics.widthPixels * 0.8f).toInt()
        height = WindowManager.LayoutParams.WRAP_CONTENT
        gravity = Gravity.CENTER
    }

    // self define dialog fragment attributes
    @Deprecated("use dialogFragmentDisplayConfigs replaced", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("dialogFragmentDisplayConfigs()"))
    open fun dialogFragmentParamConfigs() = IntArray(3) {
        when (it) {
            0 -> (screenWidth * 0.75).toInt()
            1 -> WindowManager.LayoutParams.WRAP_CONTENT
            else -> Gravity.CENTER
        }
    }

    // self define dialog fragment background
    @Deprecated("use dialogFragmentDisplayConfigs replaced", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("dialogFragmentDisplayConfigs()"))
    open fun dialogFragmentBackground() = ColorDrawable(Color.TRANSPARENT)
}