package com.kuky.comvvmhelper.ui.activity

import android.os.Bundle
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityImageDisplayBinding

@ActivityConfig(statusBarColorString = "#008577")
class ImageDisplayActivity : BaseActivity<ActivityImageDisplayBinding>() {

    override fun layoutId() = R.layout.activity_image_display

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.backgroundRemotePath = "https://t7.baidu.com/it/u=2749005241,3756993511&fm=193&f=GIF"
        mBinding.drawableId = R.drawable.ava
    }
}