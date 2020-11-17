package com.kuky.comvvmhelper.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.kk.android.comvvmhelper.entity.DialogDisplayConfig
import com.kk.android.comvvmhelper.ui.BaseDialogFragment
import com.kk.android.comvvmhelper.utils.dp2px
import com.kk.android.comvvmhelper.utils.screenHeight
import com.kk.android.comvvmhelper.utils.screenWidth
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.DialogDemoBinding

/**
 * @author kuky.
 * @description
 */
class DemoDialogFragment : BaseDialogFragment<DialogDemoBinding>() {

    override fun layoutId() = R.layout.dialog_demo

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        mBinding.dialogInfo.textSize = 18f.dp2px()
    }

    // call showAllowStateLoss replace show
    override fun showAllowStateLoss(manager: FragmentManager, tag: String) {
        super.showAllowStateLoss(manager, tag)
    }

    override fun dialogFragmentAnim() = R.style.Animation_Design_BottomSheetDialog // dialogFragment animation(not necessary)

    override fun dialogFragmentDisplayConfigs() = DialogDisplayConfig(
        (screenWidth * 0.8).toInt(), (screenHeight * 0.5).toInt(), dialogBackground = ColorDrawable(Color.WHITE)
    )
}