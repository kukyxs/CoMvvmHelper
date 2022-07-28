package com.kuky.comvvmhelper.ui.dialog

import android.os.Bundle
import android.view.View
import com.kk.android.comvvmhelper.anno.DialogConfig
import com.kk.android.comvvmhelper.ui.BaseDialogFragment
import com.kk.android.comvvmhelper.utils.dp2px
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.DialogDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author kuky.
 * @description
 */
@AndroidEntryPoint
@DialogConfig(widthFraction = 0.8f, heightFraction = 0.5f, backgroundColor = "#FFFF0000")
class DemoDialogFragment @Inject constructor() : BaseDialogFragment<DialogDemoBinding>() {

    override fun layoutId() = R.layout.dialog_demo

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        mBinding.dialogInfo.textSize = 18f.dp2px()
    }

    override fun dialogFragmentAnim() = R.style.Animation_Design_BottomSheetDialog // dialogFragment animation(not necessary)

//    override fun dialogFragmentDisplayConfigs() = DialogDisplayConfig(
//        (screenWidth * 0.8).toInt(), (screenHeight * 0.5).toInt(), dialogBackground = ColorDrawable(Color.WHITE)
//    )
}