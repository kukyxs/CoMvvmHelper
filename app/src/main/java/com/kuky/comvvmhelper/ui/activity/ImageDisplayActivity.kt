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
        mBinding.imagePath = "https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF"
    }
}